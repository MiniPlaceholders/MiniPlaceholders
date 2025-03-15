package io.github.miniplaceholders.common;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.common.loader.ExpansionProviderLoader;

import java.nio.file.Path;
import java.util.List;

public interface PlaceholdersPlugin {
    void loadDefaultExpansions();

    default void registerPlatformCommand() {
    }

    default List<Expansion> loadProvidedExpansions(Path providersFolderDirectory) throws Exception {
        return ExpansionProviderLoader.loadProvidersFromFolder(providersFolderDirectory)
                .stream()
                .filter(provider -> shouldLoad(provider.loadRequirement()))
                .map(ExpansionProvider::provideExpansion)
                .peek(Expansion::register)
                .toList();
    }

    default boolean shouldLoad(final LoadRequirement loadRequirement) {
        return switch (loadRequirement) {
            case LoadRequirement.PlatformRequirement platformRequirement -> {
                for (final Platform supportedPlatform : platformRequirement.platform()) {
                    if (supportedPlatform == MiniPlaceholders.platform()) {
                        yield true;
                    }
                }
                yield false;
            }
            case LoadRequirement.AvailableComplementRequirement complementRequirement -> {
                if (platformHasComplementLoaded(complementRequirement.name())) {
                    yield true;
                }
                for (final String platformAlias : complementRequirement.platformAliases()) {
                    if (platformHasComplementLoaded(platformAlias)) {
                        yield true;
                    }
                }
                yield false;
            }
            case LoadRequirement.MultiLoadRequirement multiLoadRequirement -> {
                for (final LoadRequirement requirement : multiLoadRequirement.requirements()) {
                    if (!this.shouldLoad(requirement)) {
                        yield false;
                    }
                }
                yield true;
            }
            // NoneRequirement
            default -> true;
        };
    }

    boolean platformHasComplementLoaded(String complementName);
}
