package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.util.TriState;

@FunctionalInterface
public interface PermissionTester {
  TriState permissionValue(Audience audience, String permission);
}
