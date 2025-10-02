package io.github.miniplaceholders.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.miniplaceholders.common.command.node.ExpansionsNode;
import io.github.miniplaceholders.common.command.node.HelpNode;
import io.github.miniplaceholders.common.command.node.ParseNode;
import io.github.miniplaceholders.common.command.node.RootNode;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BrigadierCommandProvider {
  public static <A> LiteralCommandNode<A> provideCommand(
      String command,
      PlayerSuggestions playersSuggestions,
      AudienceConverter audienceConverter,
      AudienceExtractor<A> audienceExtractor,
      PermissionTester permissionChecker
  ) {
    // Inspired by MiniMotd command implementation (MIT licenced)
    class WrappingExecutor implements Command<A> {
      private final PlaceholderCommandExecutor executor;

      private WrappingExecutor(final PlaceholderCommandExecutor executor) {
        this.executor = executor;
      }

      @Override
      public int run(CommandContext<A> commandContext) {
        executor.execute(audienceExtractor.extract(commandContext.getSource()));
        return Command.SINGLE_SUCCESS;
      }
    }

    final LiteralArgumentBuilder<A> builder = LiteralArgumentBuilder.literal(command);
    final ExpansionsNode expansionsNode = new ExpansionsNode(permissionChecker);
    final HelpNode helpNode = new HelpNode(permissionChecker);
    final ParseNode parseNode = new ParseNode(playersSuggestions, audienceConverter, permissionChecker);
    final RootNode rootNode = new RootNode(permissionChecker);

    return builder
        .requires(src -> rootNode.hasPermission(audienceExtractor.extract(src)))
        .executes(new WrappingExecutor(rootNode::execute))
        .then(LiteralArgumentBuilder.<A>literal("expansions")
            .requires(src -> expansionsNode.hasPermission(audienceExtractor.extract(src)))
            .executes(new WrappingExecutor(expansionsNode::showExpansions))
            .build()
        )
        .then(LiteralArgumentBuilder.<A>literal("help")
            .executes(new WrappingExecutor(helpNode::execute))
            .requires(src -> helpNode.hasPermission(audienceExtractor.extract(src)))
        )
        .then(LiteralArgumentBuilder.<A>literal("parse")
            .requires(src -> parseNode.hasPermission(audienceExtractor.extract(src)))
            .then(RequiredArgumentBuilder.<A, String>argument("source", StringArgumentType.word())
                .suggests((ctx, suggestionsBuilder) -> {
                  parseNode.providePlayerSuggestions().forEach(suggestionsBuilder::suggest);
                  return suggestionsBuilder.buildFuture();
                })
                .then(RequiredArgumentBuilder.<A, String>argument("message", StringArgumentType.greedyString())
                    .executes(ctx -> {
                      final Audience audience = audienceExtractor.extract(ctx.getSource());
                      final String source = StringArgumentType.getString(ctx, "source");
                      final String message = StringArgumentType.getString(ctx, "message");

                      parseNode.parseString(audience, source, message);

                      return Command.SINGLE_SUCCESS;
                    })
                )
            )
        )
        .build();
  }
}
