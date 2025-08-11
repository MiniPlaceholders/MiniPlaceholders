package io.github.miniplaceholders.common;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.common.loader.ExpansionProviderLoader;
import io.github.miniplaceholders.common.loader.FailedToLoadExpansion;
import io.github.miniplaceholders.common.loader.ProviderLoadResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface PlaceholdersPlugin {
    default void registerPlatformCommand() {
    }

    default void loadProvidedExpansions(Path providersFolderDirectory) throws Exception {
        final List<Expansion> loadedExpansions = new ArrayList<>();
        for (ExpansionProvider provider : ExpansionProviderLoader.loadProvidersFromFolder(providersFolderDirectory)) {
            ProviderLoadResult loadResult = tryLoad(provider, provider.loadRequirement());
            if (loadResult.expansion() != null) {
                loadedExpansions.add(loadResult.expansion());
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

        final String expansionsInfo = loadedExpansions.stream()
                .map(Expansion::shortToString)
                .collect(Collectors.joining(","));
        logInfo(Component.text("Loaded expansions: " + expansionsInfo, NamedTextColor.GREEN));
    }

    default ProviderLoadResult tryLoad(final ExpansionProvider provider, final LoadRequirement loadRequirement) {
        return switch (loadRequirement) {
            case LoadRequirement.PlatformRequirement platformRequirement -> {
                for (final Platform supportedPlatform : platformRequirement.platforms()) {
                    if (supportedPlatform == MiniPlaceholders.platform()) {
                        yield ProviderLoadResult.ofLoaded(provider.provideExpansion());
                    }
                }
                yield ProviderLoadResult.ofFailed(platformRequirement);
            }
            case LoadRequirement.AvailableComplementRequirement complementRequirement ->
                complementRequirement.shouldLoad(this::platformHasComplementLoaded)
                        ? ProviderLoadResult.ofLoaded(provider.provideExpansion())
                        : ProviderLoadResult.ofFailed(complementRequirement);
            case LoadRequirement.MultiLoadRequirement multiLoadRequirement -> {
                ProviderLoadResult loadResult;
                for (final LoadRequirement requirement : multiLoadRequirement.requirements()) {
                    if ((loadResult = this.tryLoad(provider, requirement)).failed() != null) {
                        yield loadResult;
                    }
                }
                yield ProviderLoadResult.ofLoaded(provider.provideExpansion());
            }
            // NoneRequirement
            default -> ProviderLoadResult.ofLoaded(provider.provideExpansion());
        };
    }

    boolean platformHasComplementLoaded(String complementName);

    void logError(Component component);

    void logInfo(Component component);
}
