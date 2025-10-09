package io.github.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.common.command.brigadier.BrigadierCommandProvider;
import io.github.miniplaceholders.common.metrics.LoadedExpansionsMetric;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bstats.charts.SingleLineChart;
import org.bstats.velocity.Metrics;

import java.nio.file.Path;
import java.util.ArrayList;
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
  @DataDirectory
  private Path dataDirectory;
  @Inject
  private ProxyServer proxyServer;
  @Inject
  private CommandManager commandManager;
  @Inject
  private ComponentLogger componentLogger;
  @Inject
  private Metrics.Factory metricsFactory;

  @Subscribe(priority = 999)
  public void onEarlyProxyInitialize(final ProxyInitializeEvent event) {
    InternalPlatform.platform(InternalPlatform.VELOCITY);
    componentLogger.info(Component.text("Starting MiniPlaceholders Velocity", NamedTextColor.GREEN));

    this.registerPlatformCommand();

    final Metrics metrics = metricsFactory.make(this, 27516);
    metrics.addCustomChart(new SingleLineChart("expansions_count", new LoadedExpansionsMetric()));
  }

  @Subscribe(priority = -404)
  public void onLateProxyInitialize(final ProxyInitializeEvent event) {
    try {
      this.loadProvidedExpansions(dataDirectory.resolve("expansions"));
    } catch (Throwable e) {
      componentLogger.error("Unable to load expansion providers", e);
    }
  }

  @Override
  public void registerPlatformCommand() {
    final LiteralCommandNode<CommandSource> node = BrigadierCommandProvider.provideCommand(
        "vminiplaceholders",
        () -> this.proxyServer.getAllPlayers()
            .stream()
            .map(Player::getUsername)
            .collect(Collectors.toCollection(ArrayList::new)),
        str -> this.proxyServer.getPlayer(str).orElse(null),
        aud -> aud,
        (audience, permission) -> ((CommandSource) audience).getPermissionValue(permission)
            .toAdventureTriState()
    );
    final BrigadierCommand command = new BrigadierCommand(node);
    final CommandMeta meta = this.commandManager.metaBuilder(command)
        .plugin(this)
        .build();
    this.commandManager.register(meta, command);
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return this.proxyServer.getPluginManager().isLoaded(complementName);
  }

  @Override
  public void logError(Component component) {
    componentLogger.error(component);
  }

  @Override
  public void logInfo(Component component) {
    componentLogger.info(component);
  }

  @Override
  public Object platformServerInstance() {
    return this.proxyServer;
  }
}
