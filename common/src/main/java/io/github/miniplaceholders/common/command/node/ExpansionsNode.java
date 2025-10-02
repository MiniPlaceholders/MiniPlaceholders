package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.common.command.PermissionTester;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

import static io.github.miniplaceholders.common.command.CommandConstants.FOOTER;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public record ExpansionsNode(PermissionTester permissionTester) implements Node {
  public void showExpansions(Audience audience) {
    final TextComponent.Builder builder = Component.text()
        .appendNewline()
        .append(miniMessage().deserialize(
            "<gradient:aqua:white:aqua><st><b>          </st> <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> Expansions <st><b>          </gradient>"
        ))
        .appendNewline();
    final Collection<Expansion> expansions = MiniPlaceholders.expansionsAvailable();
    if (expansions.isEmpty()) {
      builder.append(miniMessage().deserialize("""
              <aqua>It seems that you don't have any <#a1ff4a>Expansion</#a1ff4a> installed yet,
              You can find the <#a1ff4a>Expansion</#a1ff4a> you want here"""))
          .appendSpace()
          .append(Component.text()
              .content("[MiniPlaceholders Expansions]")
              .color(NamedTextColor.BLUE)
              .clickEvent(ClickEvent.openUrl("https://miniplaceholders.github.io/docs/user-guide/Placeholders"))
              .hoverEvent(Component.text("Search Expansions", NamedTextColor.GOLD)));
    } else {
      builder.append(expansions.stream()
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
            if (expansionVersion != null) {
              expansionMetaBuilder.appendNewline()
                  .append(Component.text("Version: ", TextColor.color(0xff7b61)))
                  .append(Component.text(expansionVersion, NamedTextColor.GRAY));
            }
            return Component.text()
                .content(expansion.name())
                .hoverEvent(expansionMetaBuilder.build())
                .build();
          }).collect(Component.toComponent(Component.text(" | "))));
    }
    builder.appendNewline().append(FOOTER);
    audience.sendMessage(builder.build());
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return permissionTester.permissionValue(audience, permission()).toBooleanOrElse(false);
  }

  @Override
  public String permission() {
    return "miniplaceholders.command.expansions";
  }
}
