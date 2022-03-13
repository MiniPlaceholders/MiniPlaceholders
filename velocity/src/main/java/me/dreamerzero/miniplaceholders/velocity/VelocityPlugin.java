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

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.api.enums.Platform;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.common.PluginConstants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

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
    @SuppressWarnings("deprecation")
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholders Velocity");

        MiniPlaceholders.setPlatform(Platform.VELOCITY);

        PlaceholdersCommand<CommandSource> command = new PlaceholdersCommand<>(
            () -> proxy.getAllPlayers().stream().map(Player::getUsername).toList(),
            (String st) -> proxy.getPlayer(st).orElse(null));
        BrigadierCommand brigadierCMD = new BrigadierCommand(command.placeholderTestCommand("vminiplaceholders"));
        proxy.getCommandManager().register(brigadierCMD);

        this.loadDefaultExpansions();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("proxy")
            .globalPlaceholder("online_players", (queue, ctx) -> {
                if(queue.hasNext()){
                    String server = queue.pop().toString();
                    return Tag.selfClosingInserting(Component.text(proxy.getServer(server).map(sv -> sv.getPlayersConnected().size()).orElse(0)));
                }
                return Tag.selfClosingInserting(Component.text(proxy.getPlayerCount()));
            })
            .globalPlaceholder("server_count", (queue, ctx) -> Tag.selfClosingInserting(Component.text(proxy.getAllServers().size())))
            .globalPlaceholder("is_player_online", (queue, ctx) -> {
                String playerName = queue.popOr(() -> "you need to introduce an argument").toString();
                return Tag.selfClosingInserting(Component.text(proxy.getPlayer(playerName).isPresent()));
            })
            .globalPlaceholder("version", (queue, ctx) -> Tag.selfClosingInserting(Component.text(proxy.getVersion().getVersion())))
        .build()
        .register();
    }
}
