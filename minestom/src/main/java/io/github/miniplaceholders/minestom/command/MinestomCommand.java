package io.github.miniplaceholders.minestom.command;

import io.github.miniplaceholders.common.command.AudienceConverter;
import io.github.miniplaceholders.common.command.PermissionTester;
import io.github.miniplaceholders.common.command.PlaceholderCommandExecutor;
import io.github.miniplaceholders.common.command.PlayersNameProvider;
import io.github.miniplaceholders.common.command.node.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MinestomCommand extends Command {
    private final PermissionTester permissionTester;
    private final PlayersNameProvider playerSuggestions;
    private final AudienceConverter audienceConverter;

    public MinestomCommand(PermissionTester permissionTester) {
        super("miniplaceholders");

        this.permissionTester = permissionTester;
        this.playerSuggestions = () -> MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .map(PlainTextComponentSerializer.plainText()::serialize)
                .collect(Collectors.toCollection(ArrayList::new));
        this.audienceConverter = string -> MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(string);

        final RootNode rootNode = new RootNode(permissionTester);

        this.setCondition(new WrappingCondition(rootNode::hasPermission));
        this.setDefaultExecutor(new WrappingExecutor(rootNode::execute));

        this.addSubcommands(new ExpansionsCommand(), new HelpCommand(), new ParseCommand(), new ParseRelCommand());
    }

    private class ExpansionsCommand extends Command {
        public ExpansionsCommand() {
            super("expansions");

            final ExpansionsNode expansionsNode = new ExpansionsNode(permissionTester);

            this.setCondition(new WrappingCondition(expansionsNode::hasPermission));
            this.setDefaultExecutor(new WrappingExecutor(expansionsNode::showExpansions));
        }
    }

    private class HelpCommand extends Command {
        public HelpCommand() {
            super("help");

            final HelpNode helpNode = new HelpNode(permissionTester);

            this.setCondition(new WrappingCondition(helpNode::hasPermission));
            this.setDefaultExecutor(new WrappingExecutor(helpNode::execute));
        }
    }

    private class ParseCommand extends Command {
        public ParseCommand() {
            super("parse");

            final ParseNode parseNode = new ParseNode(playerSuggestions, audienceConverter, permissionTester);
            final Argument<String> sourceArgument = ArgumentType.String("source")
                    .setSuggestionCallback((_, _, suggestion) -> parseNode.providePlayerSuggestions()
                            .stream()
                            .filter(s -> {
                                final String[] args = suggestion.getInput().replace("\0", "").split(" ");
                                return args.length < 3 || s.toLowerCase().startsWith(args[2].toLowerCase());
                            })
                            .map(SuggestionEntry::new)
                            .forEach(suggestion::addEntry));
            final Argument<String[]> messageArgument = ArgumentType.StringArray("message");

            this.setCondition(new WrappingCondition(parseNode::hasPermission));
            this.addSyntax((sender, context) -> {
                final String source = context.get(sourceArgument);
                final String[] message = context.get(messageArgument);

                parseNode.parseString(sender, source, String.join(" ", message));
            }, sourceArgument, messageArgument);
        }
    }

    private class ParseRelCommand extends Command {
        public ParseRelCommand() {
            super("parserel");

            final ParseRelNode parseRelNode = new ParseRelNode(playerSuggestions, audienceConverter, permissionTester);
            final Argument<String> sourceArgument = ArgumentType.String("source")
                    .setSuggestionCallback((_, _, suggestion) -> parseRelNode.providePlayerSuggestions()
                            .stream()
                            .filter(s -> {
                                final String[] args = suggestion.getInput().replace("\0", "").split(" ");
                                return args.length < 3 || s.toLowerCase().startsWith(args[2].toLowerCase());
                            })
                            .map(SuggestionEntry::new)
                            .forEach(suggestion::addEntry));
            final Argument<String> relationalSourceArgument = ArgumentType.String("relational")
                    .setSuggestionCallback((_, _, suggestion) -> parseRelNode.providePlayerSuggestions()
                            .stream()
                            .filter(s -> {
                                final String[] args = suggestion.getInput().replace("\0", "").split(" ");
                                return args.length < 4 || s.toLowerCase().startsWith(args[3].toLowerCase());
                            })
                            .map(SuggestionEntry::new)
                            .forEach(suggestion::addEntry));
            final Argument<String[]> messageArgument = ArgumentType.StringArray("message");

            this.setCondition(new WrappingCondition(parseRelNode::hasPermission));
            this.addSyntax((sender, context) -> {
                final String source = context.get(sourceArgument);
                final String relationalSource = context.get(relationalSourceArgument);
                final String[] message = context.get(messageArgument);

                parseRelNode.parseString(sender, source, relationalSource, String.join(" ", message));
            }, sourceArgument, relationalSourceArgument, messageArgument);
        }
    }

    private record WrappingCondition(Predicate<Audience> predicate) implements CommandCondition {
        @Override
        public boolean canUse(@NonNull CommandSender sender, @Nullable String commandString) {
            return predicate.test(sender);
        }
    }

    private record WrappingExecutor(PlaceholderCommandExecutor executor) implements CommandExecutor {
        @Override
        public void apply(@NonNull CommandSender sender, @NonNull CommandContext context) {
            executor.execute(sender);
        }
    }
}
