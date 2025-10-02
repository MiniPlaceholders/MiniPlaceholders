package io.github.miniplaceholders.common.loader;

import io.github.miniplaceholders.api.provider.LoadRequirement;
import org.jspecify.annotations.NonNull;

public record FailedToLoadExpansion(@NonNull LoadRequirement requirement) {
}
