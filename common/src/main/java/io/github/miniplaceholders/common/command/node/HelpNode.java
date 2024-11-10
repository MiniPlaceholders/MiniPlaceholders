package io.github.miniplaceholders.common.command.node;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.Command;

import static io.github.miniplaceholders.common.command.PlaceholdersCommand.FOOTER;
import static io.github.miniplaceholders.common.command.PlaceholdersCommand.HEADER;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class HelpNode<S extends Audience> implements Node<S> {
  private static final Component HELP = text()
          .append(newline(), HEADER)
          .append(newline(), miniMessage().deserialize(
                  "<gradient:#9694ff:#5269ff>Commands:</gradient>"))
          .append(newline(), miniMessage().deserialize(
                  "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>"))
          .append(newline(), miniMessage().deserialize(
                  "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <#8fadff><player | me></#8fadff> <#99ffb6><player></#99ffb6>"))
          .append(newline(), FOOTER)
          .build();

  @Override
  public Command.Builder<S> apply(Command.Builder<S> rootBuilder) {
    return rootBuilder.literal("help")
            .permission("miniplaceholders.command.help")
            .handler(handler -> handler.sender().sendMessage(HELP));
  }
}
