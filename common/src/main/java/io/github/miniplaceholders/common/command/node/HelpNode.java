package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.common.command.PermissionTester;
import net.kyori.adventure.audience.Audience;

import static io.github.miniplaceholders.common.command.CommandConstants.HELP;

public record HelpNode(PermissionTester permissionChecker) implements Node {
  public void execute(Audience audience) {
    audience.sendMessage(HELP);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return permissionChecker.permissionValue(audience, permission()).toBooleanOrElse(false);
  }

  @Override
  public String permission() {
    return "miniplaceholders.command.help";
  }
}
