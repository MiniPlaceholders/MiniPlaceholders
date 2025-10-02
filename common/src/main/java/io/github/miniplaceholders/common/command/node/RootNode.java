package io.github.miniplaceholders.common.command.node;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.TriState;

import static io.github.miniplaceholders.common.command.CommandConstants.INFO;

public final class RootNode implements Node {

  public void execute(Audience audience) {
    audience.sendMessage(INFO);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return audience.get(PermissionChecker.POINTER)
        .map(checker -> checker.value(permission()))
        .orElse(TriState.TRUE) != TriState.FALSE;
  }

  @Override
  public String permission() {
    return "miniplaceholders.command";
  }
}
