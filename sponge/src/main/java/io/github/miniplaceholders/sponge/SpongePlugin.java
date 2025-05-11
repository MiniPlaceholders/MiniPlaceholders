package io.github.miniplaceholders.sponge;

import com.google.inject.Inject;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.connect.InternalPlatform;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Plugin("miniplaceholders")
public class SpongePlugin implements PlaceholdersPlugin {

  @Inject
  private Logger logger;
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;

  private Server server;

  @Listener
  public void onServerStart(final StartingEngineEvent<Server> event) {
    this.server = event.engine();
    logger.info("Starting MiniPlaceholders Sponge");
    InternalPlatform.platform(InternalPlatform.SPONGE);

    try {
      final List<Expansion> loadedExpansions = this.loadProvidedExpansions(configDir.resolve("expansions"));
      if (loadedExpansions.isEmpty()) {
        logger.info("Not found expansions");
      } else {
        final String expansionsInfo = loadedExpansions.stream()
                .map(Expansion::shortToString)
                .collect(Collectors.joining(","));
        logger.info("Loaded expansions: {}", expansionsInfo);
      }
    } catch (Throwable e) {
      logger.error("Unable to load expansion providers", e);
    }
  }

  private final AtomicInteger registration = new AtomicInteger(0);

  @Listener
  public void onCommandRegister(final RegisterCommandEvent<Command.Parameterized> event) {
    if (registration.getAndIncrement() == 0) {
      this.registerPlatformCommand();
    }
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return server.game().pluginManager().plugin(complementName).isPresent();
  }
}
