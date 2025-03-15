package io.github.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.CloudInjectionModule;
import org.incendo.cloud.velocity.VelocityCommandManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Plugin(
        name = "MiniPlaceholders",
        id = "miniplaceholders",
        version = PluginConstants.VERSION,
        authors = {"MiniPlaceholders Contributors", "4drian3d"},
        description = "MiniMessage Component-based Placeholders API"
)
public final class VelocityPlugin implements PlaceholdersPlugin {
  @Inject
  private Injector injector;
  @Inject
  @DataDirectory
  private Path dataDirectory;
  @Inject
  private ProxyServer proxyServer;
  @Inject
  private ComponentLogger componentLogger;

  @Subscribe
  public void onProxyInitialize(final ProxyInitializeEvent event) {
    componentLogger.info(Component.text("Starting MiniPlaceholders Velocity", NamedTextColor.GREEN));

    InternalPlatform.platform(InternalPlatform.VELOCITY);

    this.loadDefaultExpansions();
    this.registerPlatformCommand();

    try {
      final List<Expansion> loadedExpansions = this.loadProvidedExpansions(dataDirectory.resolve("expansions"));
      if (loadedExpansions.isEmpty()) {
        componentLogger.info(Component.text("Not found expansions", NamedTextColor.GRAY));
      } else {
        final String expansionsInfo = loadedExpansions.stream()
                    .map(Expansion::shortToString)
                    .collect(Collectors.joining(","));
        componentLogger.info(Component.text("Loaded expansions: " + expansionsInfo, NamedTextColor.GREEN));
      }
    } catch (Throwable e) {
      componentLogger.error("Unable to load expansion providers", e);
    }
  }

  @Override
  public void loadDefaultExpansions() {
    Expansion.builder("proxy")
            .author("MiniPlaceholders Contributors")
            .version(PluginConstants.VERSION)
            .globalPlaceholder("online_players", (queue, ctx) -> {
              if (queue.hasNext()) {
                String server = queue.pop().value();
                return Tag.preProcessParsed(proxyServer.getServer(server)
                        .map(sv -> sv.getPlayersConnected().size())
                        .map(size -> Integer.toString(size))
                        .orElse("0"));
              }
              return Tag.preProcessParsed(Integer.toString(proxyServer.getPlayerCount()));
            })
            .globalPlaceholder("server_count", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(proxyServer.getAllServers().size())))
            .globalPlaceholder("is_player_online", (queue, ctx) -> {
              String playerName = queue.popOr(() -> "you need to introduce an argument").value();
              return Tag.selfClosingInserting(proxyServer.getPlayer(playerName).isPresent() ? Components.YES_COMPONENT : Components.NO_COMPONENT);
            })
            .globalPlaceholder("version", Tag.preProcessParsed(proxyServer.getVersion().getVersion()))
            .build()
            .register();
  }

  @Override
  public void registerPlatformCommand() {
    injector = injector.createChildInjector(
            new CloudInjectionModule<>(
                    CommandSource.class,
                    ExecutionCoordinator.simpleCoordinator(),
                    SenderMapper.identity()
            )
    );
    final VelocityCommandManager<CommandSource> commandManager = injector.getInstance(
            Key.get(new TypeLiteral<>() {
            })
    );
    PlaceholdersCommand.<CommandSource>builder()
            .playerSuggestions(() -> proxyServer.getAllPlayers()
                    .stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toCollection(ArrayList::new)))
            .toAudience(st -> proxyServer.getPlayer(st).orElse(null))
            .command("vminiplaceholders")
            .manager(commandManager)
            .build().register();
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return this.proxyServer.getPluginManager().isLoaded(complementName);
  }
}
