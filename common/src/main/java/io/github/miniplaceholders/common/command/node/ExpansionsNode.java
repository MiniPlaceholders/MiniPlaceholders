package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.incendo.cloud.Command;

import static io.github.miniplaceholders.common.command.PlaceholdersCommand.FOOTER;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class ExpansionsNode<S extends Audience> implements Node<S> {
  @Override
  public Command.Builder<S> apply(Command.Builder<S> rootBuilder) {
    return rootBuilder.literal("expansions")
            .permission("miniplaceholders.command.expansions")
            .handler(handler -> {
              TextComponent.Builder builder = Component.text()
                      .appendNewline()
                      .append(miniMessage().deserialize(
                              "<gradient:aqua:white:aqua><st><b>          </st> <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> Expansions <st><b>          </gradient>"
                      ))
                      .appendNewline()
                      .append(MiniPlaceholders.getExpansionsAvailable()
                              .map(expansion -> {
                                final TextComponent.Builder expansionMetaBuilder = Component.text();
                                final String expansionName = expansion.name();
                                final String expansionAuthor = expansion.author();
                                final String expansionVersion = expansion.version();

                                expansionMetaBuilder.append(Component.text(expansionName, NamedTextColor.GRAY));
                                if (expansionAuthor != null) {
                                  expansionMetaBuilder.appendNewline()
                                          .append(Component.text("Author: ", TextColor.color(0x29fbff)))
                                          .append(Component.text(expansionAuthor, NamedTextColor.GRAY));
                                }
                                if (expansionVersion != null){
                                  expansionMetaBuilder.appendNewline()
                                          .append(Component.text("Version: ", TextColor.color(0xff7b61)))
                                          .append(Component.text(expansionVersion, NamedTextColor.GRAY));
                                }
                                return Component.text()
                                        .content(expansion.name())
                                        .hoverEvent(expansionMetaBuilder.build())
                                        .build();
                              }).collect(Component.toComponent(Component.text(" | "))))
                      .appendNewline()
                      .append(FOOTER);
              handler.sender().sendMessage(builder.build());
            });
  }
}
