package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.common.command.PermissionTester;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NullMarked;

import static io.github.miniplaceholders.common.command.CommandConstants.INFO;

@NullMarked
public record RootNode(PermissionTester permissionChecker) implements Node {

  public void execute(Audience audience) {
    audience.sendMessage(INFO);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return permissionChecker.permissionValue(audience, permission()) != TriState.FALSE;
  }

  @Override
  public String permission() {
    return "miniplaceholders.command";
  }
}
