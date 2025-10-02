package io.github.miniplaceholders.sponge;

import com.google.inject.Inject;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.command.AudienceExtractor;
import io.github.miniplaceholders.common.command.PlaceholderCommandExecutor;
import io.github.miniplaceholders.common.command.node.ExpansionsNode;
import io.github.miniplaceholders.common.command.node.HelpNode;
import io.github.miniplaceholders.common.command.node.ParseNode;
import io.github.miniplaceholders.common.command.node.RootNode;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.*;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Plugin("miniplaceholders")
public class SpongePlugin implements PlaceholdersPlugin {

  @Inject
  private Logger logger;
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;
  @Inject
  private PluginContainer pluginContainer;

  private Server server;

  @Listener
  public void onServerStart(final StartingEngineEvent<Server> event) {
    this.server = event.engine();
    logger.info("Starting MiniPlaceholders Sponge");
    InternalPlatform.platform(InternalPlatform.SPONGE);

    try {
      this.loadProvidedExpansions(configDir.resolve("expansions"));
    } catch (Throwable e) {
      logger.error("Unable to load expansion providers", e);
    }
  }

  @Listener
  public void onCommandRegister(final RegisterCommandEvent<Command.Parameterized> event) {
    final AudienceExtractor<CommandCause> audienceExtractor = CommandCause::audience;
    final RootNode rootNode = new RootNode();
    final ExpansionsNode expansionsNode = new ExpansionsNode();
    final ParseNode parseNode = new ParseNode(
        () -> this.server.onlinePlayers()
            .stream()
            .map(ServerPlayer::user)
            .map(User::name)
            .collect(Collectors.toCollection(ArrayList::new)),
        string -> this.server.player(string).orElse(null)
    );
    final HelpNode helpNode = new HelpNode();

    final Parameter.Value<String> sourceParameter = Parameter.string().key("source")
        .completer((context, currentInput) -> parseNode.providePlayerSuggestions()
            .stream()
            .map(CommandCompletion::of)
            .toList()
        ).build();
    final Parameter.Value<String> messageParameter = Parameter.remainingJoinedStrings().key("message").build();

    final Command.Parameterized node = Command.builder()
        .executionRequirements(src -> rootNode.hasPermission(audienceExtractor.extract(src)))
        .executor(new WrappingExecutor(rootNode::execute))
        .addChildren(Map.of(
            List.of("expansions"), Command.builder()
                .executionRequirements(cause -> expansionsNode.hasPermission(audienceExtractor.extract(cause)))
                .executor(new WrappingExecutor(expansionsNode::showExpansions))
                .build(),
            List.of("help"), Command.builder()
                .executionRequirements(cause -> helpNode.hasPermission(audienceExtractor.extract(cause)))
                .executor(new WrappingExecutor(helpNode::execute))
                .build(),
            List.of("parse"), Command.builder()
                .executionRequirements(cause -> parseNode.hasPermission(audienceExtractor.extract(cause)))
                .addParameter(sourceParameter)
                .addParameter(messageParameter)
                .executor(ctx -> {
                  final Audience executor = audienceExtractor.extract(ctx.cause());
                  final String source = ctx.requireOne(sourceParameter);
                  final String message = ctx.requireOne(messageParameter);

                  parseNode.parseString(executor, source, message);

                  return CommandResult.success();
                })
                .build()
        ))
        .build();


    event.register(pluginContainer, node, "miniplaceholders");
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return server.game().pluginManager().plugin(complementName).isPresent();
  }

  // TODO: Replace this with some implementation of a ComponentLogger
  @Override
  public void logError(Component component) {
    logger.error(PlainTextComponentSerializer.plainText().serialize(component));
  }

  @Override
  public void logInfo(Component component) {
    logger.info(PlainTextComponentSerializer.plainText().serialize(component));
  }

  @Override
  public Object platformServerInstance() {
    return this.server;
  }

  private record WrappingExecutor(PlaceholderCommandExecutor executor) implements CommandExecutor {
    @Override
    public CommandResult execute(CommandContext context) {
      executor.execute(context.cause().audience());
      return CommandResult.success();
    }
  }
}
