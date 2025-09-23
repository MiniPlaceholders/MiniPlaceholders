package io.github.miniplaceholders.common;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.provider.PlatformData;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.common.loader.ExpansionProviderLoader;
import io.github.miniplaceholders.common.loader.FailedToLoadExpansion;
import io.github.miniplaceholders.common.loader.ProviderLoadResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import team.unnamed.inject.Injector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface PlaceholdersPlugin {
  default void registerPlatformCommand() {
  }

  default void loadProvidedExpansions(Path providersFolderDirectory) throws Exception {
    final List<Expansion> loadedExpansions = new ArrayList<>();
    final PlatformData platformData = new PlatformData(this.platformServerInstance(), this);
    final Injector injector = Injector.create(binder -> binder.bind(PlatformData.class).toInstance(platformData));
    for (ExpansionProvider provider : ExpansionProviderLoader.loadProvidersFromFolder(providersFolderDirectory)) {
      injector.injectMembers(provider);
      ProviderLoadResult loadResult = tryLoad(provider, provider.loadRequirement());
      final ExpansionProvider loadedProvider = loadResult.provider();
      if (loadedProvider != null) {
        final Expansion loadedExpansion = loadedProvider.provideExpansion();
        loadedExpansions.add(loadedExpansion);
        loadedExpansion.register();
        continue;
      }
      final FailedToLoadExpansion failedToLoad = loadResult.failed();
      logError(Component.text("The expansion "
              + provider.getClass() +
              " could not be loaded because the requirement " + failedToLoad.requirement() + " could not be resolved",
          NamedTextColor.RED));
    }

    if (loadedExpansions.isEmpty()) {
      logInfo(Component.text("Not found expansions", NamedTextColor.GRAY));
      return;
    }

    final Component expansionsInfo = loadedExpansions.stream()
        .map(this::expansionInfo)
        .collect(Component.toComponent(Component.text(", ", NamedTextColor.WHITE)));
    logInfo(Component.text("Loaded expansions: ", NamedTextColor.GREEN).append(expansionsInfo));
  }

  private Component expansionInfo(Expansion expansion) {
    final TextComponent.Builder builder = Component.text();
    builder.append(Component.text(expansion.name(), NamedTextColor.AQUA));
    final String author = expansion.author();
    final String version = expansion.version();
    if (author == null && version == null) {
      return builder.build();
    }
    builder.append(Component.text('[', NamedTextColor.DARK_GRAY));
    if (author != null) {
      builder.append(
          Component.text("Author", NamedTextColor.GRAY),
          Component.text(':', NamedTextColor.WHITE),
          Component.space(),
          Component.text(author, TextColor.color(0xd4ff54))
      );
    }
    if (version != null) {
      if (author != null) {
        builder.append(Component.text(", ", NamedTextColor.WHITE));
      }
      builder.append(
          Component.text("Version", NamedTextColor.GRAY),
          Component.text(':', NamedTextColor.WHITE),
          Component.space(),
          Component.text(version, TextColor.color(0xd4ff54))
      );
    }
    builder.append(Component.text(']', NamedTextColor.DARK_GRAY));
    return builder.build();
  }

  default ProviderLoadResult tryLoad(final ExpansionProvider provider, final LoadRequirement loadRequirement) {
    return switch (loadRequirement) {
      case LoadRequirement.PlatformRequirement platformRequirement -> {
        for (final Platform supportedPlatform : platformRequirement.platforms()) {
          if (supportedPlatform == MiniPlaceholders.platform()) {
            yield ProviderLoadResult.ofLoaded(provider);
          }
        }
        yield ProviderLoadResult.ofFailed(platformRequirement);
      }
      case LoadRequirement.AvailableComplementRequirement complementRequirement ->
          complementRequirement.shouldLoad(this::platformHasComplementLoaded)
              ? ProviderLoadResult.ofLoaded(provider)
              : ProviderLoadResult.ofFailed(complementRequirement);
      case LoadRequirement.MultiLoadRequirement multiLoadRequirement -> {
        ProviderLoadResult loadResult;
        for (final LoadRequirement requirement : multiLoadRequirement.requirements()) {
          if ((loadResult = this.tryLoad(provider, requirement)).failed() != null) {
            yield loadResult;
          }
        }
        yield ProviderLoadResult.ofLoaded(provider);
      }
      case LoadRequirement.ClassRequirement classRequirement -> classRequirement.shouldLoad()
          ? ProviderLoadResult.ofLoaded(provider) : ProviderLoadResult.ofFailed(classRequirement);
      case LoadRequirement.AnyOfRequirement anyOfRequirement -> {
        ProviderLoadResult loadResult;
        for (final LoadRequirement requirement : anyOfRequirement.requirements()) {
          if ((loadResult = this.tryLoad(provider, requirement)).provider() != null) {
            yield loadResult;
          }
        }
        yield ProviderLoadResult.ofFailed(anyOfRequirement);
      }
      case LoadRequirement.NoneLoadRequirement ignored -> ProviderLoadResult.ofLoaded(provider);
    };
  }

  boolean platformHasComplementLoaded(String complementName);

  void logError(Component component);

  void logInfo(Component component);

  Object platformServerInstance();
}
