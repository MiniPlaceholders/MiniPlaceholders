package io.github.miniplaceholders.common.loader;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.LoadRequirement;

public record ProviderLoadResult(Expansion expansion, FailedToLoadExpansion failed) {
    public static ProviderLoadResult ofLoaded(Expansion expansion) {
        return new ProviderLoadResult(expansion, null);
    }

    public static ProviderLoadResult ofFailed(LoadRequirement requirement) {
        return new ProviderLoadResult(null, new FailedToLoadExpansion(requirement));
    }
}
