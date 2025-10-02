package io.github.miniplaceholders.sponge;

import com.google.inject.Inject;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.github.miniplaceholders.sponge.command.SpongeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;

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
    InternalPlatform.platform(InternalPlatform.SPONGE);
    logger.info("Starting MiniPlaceholders Sponge");

    try {
      this.loadProvidedExpansions(configDir.resolve("expansions"));
    } catch (Throwable e) {
      logger.error("Unable to load expansion providers", e);
    }
  }

  @Listener
  public void onCommandRegister(final RegisterCommandEvent<Command.Parameterized> event) {
    event.register(pluginContainer, SpongeCommand.provideCommand(), "miniplaceholders");
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

}
