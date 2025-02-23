package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PermissionTester<S extends Audience> {
  boolean hasPermission(@NotNull S source, @NotNull String permission);
}
