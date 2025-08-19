package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.common.command.AudienceConverter;
import io.github.miniplaceholders.common.command.PlayerSuggestions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.Command;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.component.TypedCommandComponent;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public record ParseNode<S extends Audience>(
        PlayerSuggestions playerSuggestions,
        AudienceConverter audienceConverter
) implements Node<S> {
  @Override
  public Command.Builder<S> apply(Command.Builder<S> rootBuilder) {
    final TypedCommandComponent<S, String> sourceArgument = CommandComponent.<S, String>ofType(String.class, "source")
            .parser(StringParser.stringParser(StringParser.StringMode.SINGLE))
            .description(Description.of("The source from which the message will be parsed"))
            .suggestionProvider(SuggestionProvider.blockingStrings((ctx, st) -> {
              final List<String> suggestions = playerSuggestions.suggest();
              suggestions.add("me");
              return suggestions;
            }))
            .build();
    final TypedCommandComponent<S, String> stringArgument = CommandComponent.<S, String>ofType(String.class, "message")
            .parser(StringParser.greedyStringParser())
            .description(Description.of("Message to be parsed"))
            .build();

    return rootBuilder.literal("parse")
            .argument(sourceArgument)
            .argument(stringArgument)
            .permission("miniplaceholders.command.parse")
            .handler(handler -> {
              final String source = handler.get(sourceArgument);

              final Audience objective = "me".equals(source)
                      ? handler.sender()
                      : audienceConverter.convert(source);
              if (objective == null){
                handler.sender().sendMessage(text("You must specify a valid player", RED));
                return;
              }
              final String toParse = handler.get(stringArgument);

              final Component parsed = miniMessage().deserialize(
                      toParse,
                      objective,
                      MiniPlaceholders.audienceGlobalPlaceholders()
              );
              handler.sender().sendMessage(parsed);
            });
  }
}
