package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.common.command.AudienceConverter;
import io.github.miniplaceholders.common.command.PermissionTester;
import io.github.miniplaceholders.common.command.PlayerSuggestions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public record ParseNode(
        PlayerSuggestions playerSuggestions,
        AudienceConverter audienceConverter,
        PermissionTester permissionChecker
) implements Node {
  public List<String> providePlayerSuggestions() {
    final List<String> suggestions = playerSuggestions.suggest();
    suggestions.add("me");
    return suggestions;
  }

  public void parseString(Audience sender, String source, String toParse) {
    final Audience objective = "me".equals(source)
        ? sender
        : audienceConverter.convert(source);
    if (objective == null) {
      sender.sendMessage(text("You must specify a valid player", RED));
      return;
    }

    final Component parsed = miniMessage().deserialize(
        toParse,
        objective,
        MiniPlaceholders.audienceGlobalPlaceholders()
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
