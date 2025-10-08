package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface AudienceConverter {
  @Nullable Audience convert(@NonNull String string);
}
