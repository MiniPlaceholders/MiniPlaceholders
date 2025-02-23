package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface AudienceConverter {
  @Nullable Audience convert(@NotNull String string);
}
