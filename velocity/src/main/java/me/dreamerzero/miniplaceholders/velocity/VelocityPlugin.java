package me.dreamerzero.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.common.PluginConstants;

@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = PluginConstants.VERSION,
    authors = {"4drian3d"}
)
public final class VelocityPlugin implements PlaceholdersPlugin {
    private final Logger logger;
    private final ProxyServer proxy;
    @Inject
    public VelocityPlugin(Logger logger, ProxyServer proxy) {
        this.logger = logger;
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholders Velocity");
        this.setPlatform("velocity");
        PlaceholdersCommand<CommandSource> command = new PlaceholdersCommand<>(
            () -> proxy.getAllPlayers().stream().map(Player::getUsername).toList(),
            (String st) -> proxy.getPlayer(st).orElse(null));
        BrigadierCommand brigadierCMD = new BrigadierCommand(command.placeholderTestCommand("vminiplaceholders"));
        proxy.getCommandManager().register(brigadierCMD);
    }
}
