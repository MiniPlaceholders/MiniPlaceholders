package io.github.miniplaceholders.common.command.node;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;

import static io.github.miniplaceholders.common.command.CommandConstants.FOOTER;
import static io.github.miniplaceholders.common.command.CommandConstants.HEADER;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class HelpNode implements Node {
  private static final Component HELP = text()
          .append(newline(), HEADER)
          .append(newline(), miniMessage().deserialize(
                  "<gradient:#9694ff:#5269ff>Commands:</gradient>"))
          .append(newline(), miniMessage().deserialize(
                  "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>"))
          .append(newline(), miniMessage().deserialize(
                  "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <#8fadff><player | me></#8fadff> <#99ffb6><player></#99ffb6>"))
          .append(newline(), miniMessage().deserialize(
                  "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>expansions</aqua>"))
          .append(newline(), FOOTER)
          .build();

  public void execute(Audience audience) {
    audience.sendMessage(HELP);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return audience.get(PermissionChecker.POINTER)
        .map(checker -> checker.test("miniplaceholders.command.help"))
        .orElse(false);
  }
}
