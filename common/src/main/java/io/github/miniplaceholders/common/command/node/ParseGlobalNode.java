package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.common.command.PermissionTester;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public record ParseGlobalNode(PermissionTester permissionTester) implements Node {
  public void parseString(final Audience sender, final String toParse) {
    final Component parsed = miniMessage().deserialize(
        toParse,
        MiniPlaceholders.globalPlaceholders()
    );
    sender.sendMessage(parsed);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return permissionTester.permissionValue(audience, permission()).toBooleanOrElse(false);
  }

  @Override
  public String permission() {
    return "miniplaceholders.command.parseglobal";
  }
}
