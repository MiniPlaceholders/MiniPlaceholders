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
import me.dreamerzero.miniplaceholders.api.utils.Components;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.common.PluginConstants;
import me.dreamerzero.miniplaceholders.connect.InternalPlatform;
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
    public VelocityPlugin(final Logger logger, final ProxyServer proxy) {
        this.logger = logger;
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialize(final ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholders Velocity");

        InternalPlatform.platform(InternalPlatform.VELOCITY);

        this.loadDefaultExpansions();
        this.registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("proxy")
            .globalPlaceholder("online_players", (queue, ctx) -> {
                if(queue.hasNext()){
                    String server = queue.pop().value();
                    return Tag.selfClosingInserting(Component.text(proxy.getServer(server).map(sv -> sv.getPlayersConnected().size()).orElse(0)));
                }
                return Tag.selfClosingInserting(Component.text(proxy.getPlayerCount()));
            })
            .globalPlaceholder("server_count", (queue, ctx) -> Tag.selfClosingInserting(Component.text(proxy.getAllServers().size())))
            .globalPlaceholder("is_player_online", (queue, ctx) -> {
                String playerName = queue.popOr(() -> "you need to introduce an argument").value();
                return TagsUtils.staticTag(proxy.getPlayer(playerName).isPresent() ? Components.YES_COMPONENT : Components.NO_COMPONENT);
            })
            .globalPlaceholder("version", TagsUtils.staticTag(proxy.getVersion().getVersion()))
        .build()
        .register();
    }

    @Override
    public void registerPlatformCommand() {
        final BrigadierCommand brigadierCMD = new BrigadierCommand(
            new PlaceholdersCommand<CommandSource>(
                () -> proxy.getAllPlayers().stream().map(Player::getUsername).toList(),
                (String st) -> proxy.getPlayer(st).orElse(null))
            .placeholderTestCommand("vminiplaceholders"));

        proxy.getCommandManager().register(brigadierCMD);
    }
}
