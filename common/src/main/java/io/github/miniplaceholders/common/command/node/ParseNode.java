package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.common.command.AudienceConverter;
import io.github.miniplaceholders.common.command.PermissionTester;
import io.github.miniplaceholders.common.command.PlayersNameProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@NullMarked
public record ParseNode(
        PlayersNameProvider playersNameProvider,
        AudienceConverter audienceConverter,
        PermissionTester permissionChecker
) implements Node, PlayerCompleterNode {
  public void parseString(final Audience sender, final String providedSource, final String providedString) {
    final Audience objective = switch (providedSource) {
      case "me" -> sender;
      case "--null" -> Audience.empty();
      default -> audienceConverter.convert(providedSource);
    };
    if (objective == null) {
      sender.sendMessage(text("You must specify a valid player", RED));
      return;
    }

    final TagResolver resolver = objective == Audience.empty()
        ? MiniPlaceholders.globalPlaceholders()
        : MiniPlaceholders.audienceGlobalPlaceholders();

    final Component parsed = miniMessage().deserialize(providedString, objective, resolver);
    sender.sendMessage(parsed);
  }

  @Override
  public boolean hasPermission(final Audience audience) {
    return permissionChecker.permissionValue(audience, permission()).toBooleanOrElse(false);
  }

  @Override
  public String permission() {
    return "miniplaceholders.command.parse";
  }

  @Override
  public List<String> providePlayerSuggestions() {
    final List<String> suggestions = PlayerCompleterNode.super.providePlayerSuggestions();
    suggestions.add("--null");
    return suggestions;
  }
}
