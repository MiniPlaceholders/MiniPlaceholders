package io.github.miniplaceholders.common.loader;

import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;

public record ProviderLoadResult(ExpansionProvider provider, FailedToLoadExpansion failed) {
    public static ProviderLoadResult ofLoaded(ExpansionProvider provider) {
        return new ProviderLoadResult(provider, null);
    }

    public static ProviderLoadResult ofFailed(LoadRequirement requirement) {
        return new ProviderLoadResult(null, new FailedToLoadExpansion(requirement));
    }
}
