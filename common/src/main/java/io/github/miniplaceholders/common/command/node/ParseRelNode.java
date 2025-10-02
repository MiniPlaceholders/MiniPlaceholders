package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.types.RelationalAudience;
import io.github.miniplaceholders.common.command.AudienceConverter;
import io.github.miniplaceholders.common.command.PermissionTester;
import io.github.miniplaceholders.common.command.PlayerSuggestions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public record ParseRelNode(
    PlayerSuggestions playerSuggestions,
    AudienceConverter audienceConverter,
    PermissionTester permissionChecker
) implements Node, PlayerSuggestionProvider {

  public void parseString(final Audience sender, final String source, final String relational, final String toParse) {
    final Audience senderTarget = "me".equals(source)
        ? sender
        : audienceConverter.convert(source);
    if (senderTarget == null) {
      sender.sendMessage(text("You must specify a valid player", RED));
      return;
    }

    final Audience relationalTarget = "me".equals(relational)
        ? sender
        : audienceConverter.convert(relational);
    if (relationalTarget == null) {
      sender.sendMessage(text("You must specify a valid relational player", RED));
      return;
    }

    final RelationalAudience<Audience> relationalAudience = RelationalAudience.from(senderTarget, relationalTarget);

    final Component parsed = miniMessage().deserialize(
        toParse,
        relationalAudience,
        MiniPlaceholders.relationalGlobalPlaceholders()
    );
    sender.sendMessage(parsed);
  }

  @Override
  public boolean hasPermission(Audience audience) {
    return permissionChecker.permissionValue(audience, permission()).toBooleanOrElse(false);
  }

  @Override
  public String permission() {
    return "miniplaceholders.command.parse";
  }
}
