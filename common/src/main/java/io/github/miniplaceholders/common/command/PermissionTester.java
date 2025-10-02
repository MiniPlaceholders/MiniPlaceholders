package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface PermissionTester {
  TriState permissionValue(Audience audience, String permission);
}
