package io.github.miniplaceholders.sponge.command;

import io.github.miniplaceholders.common.command.*;
import io.github.miniplaceholders.common.command.node.*;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.*;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NullMarked
public final class SpongeCommand {
  public static Command.Parameterized provideCommand() {
    final AudienceExtractor<CommandCause> audienceExtractor = CommandCause::audience;
    final PermissionTester permissionTester = PermissionTester.NO_OP; // Not really used here
    final PlayersNameProvider playerSuggestions = () -> Sponge.server().onlinePlayers()
        .stream()
        .map(ServerPlayer::user)
        .map(User::name)
        .collect(Collectors.toCollection(ArrayList::new));
    final AudienceConverter audienceConverter = string -> Sponge.server().player(string).orElse(null);

    // Command Nodes
    final RootNode rootNode = new RootNode(permissionTester);
    final ExpansionsNode expansionsNode = new ExpansionsNode(permissionTester);
    final ParseNode parseNode = new ParseNode(playerSuggestions, audienceConverter, permissionTester);
    final ParseRelNode parseRelNode = new ParseRelNode(playerSuggestions, audienceConverter, permissionTester);
    final HelpNode helpNode = new HelpNode(permissionTester);

    final Parameter.Value<String> sourceParameter = Parameter.string().key("source")
        .completer((context, currentInput) -> parseNode.providePlayerSuggestions()
            .stream()
            .map(CommandCompletion::of)
            .toList()
        ).build();
    final Parameter.Value<String> relationalSourceParameter = Parameter.string().key("relational")
        .completer((context, currentInput) -> parseNode.providePlayerSuggestions()
            .stream()
            .map(CommandCompletion::of)
            .toList()
        ).build();
    final Parameter.Value<String> messageParameter = Parameter.remainingJoinedStrings().key("message").build();

    return Command.builder()
        .executionRequirements(src -> src.permissionValue(rootNode.permission()) != Tristate.FALSE)
        .executor(new WrappingExecutor(rootNode::execute))
        .addChildren(Map.of(
            List.of("expansions"), Command.builder()
                .permission(expansionsNode.permission())
                .executor(new WrappingExecutor(expansionsNode::showExpansions))
                .build(),
            List.of("help"), Command.builder()
                .permission(helpNode.permission())
                .executor(new WrappingExecutor(helpNode::execute))
                .build(),
            List.of("parse"), Command.builder()
                .permission(parseNode.permission())
                .addParameter(sourceParameter)
                .addParameter(messageParameter)
                .executor(ctx -> {
                  final Audience executor = audienceExtractor.extract(ctx.cause());
                  final String source = ctx.requireOne(sourceParameter);
                  final String message = ctx.requireOne(messageParameter);

                  parseNode.parseString(executor, source, message);

                  return CommandResult.success();
                })
                .build(),
            List.of("parserel"), Command.builder()
                .permission(parseRelNode.permission())
                .addParameter(sourceParameter)
                .addParameter(relationalSourceParameter)
                .addParameter(messageParameter)
                .executor(ctx -> {
                  final Audience executor = audienceExtractor.extract(ctx.cause());
                  final String source = ctx.requireOne(sourceParameter);
                  final String relational = ctx.requireOne(relationalSourceParameter);
                  final String message = ctx.requireOne(messageParameter);

                  parseRelNode.parseString(executor, source, relational, message);

                  return CommandResult.success();
                })
                .build()
        ))
        .build();
  }

  private record WrappingExecutor(PlaceholderCommandExecutor executor) implements CommandExecutor {
    @Override
    public CommandResult execute(CommandContext context) {
      executor.execute(context.cause().audience());
      return CommandResult.success();
    }
  }
}
