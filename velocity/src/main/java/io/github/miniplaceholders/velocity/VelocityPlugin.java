package io.github.miniplaceholders.velocity;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.CloudInjectionModule;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.api.utils.TagsUtils;
import io.github.miniplaceholders.common.PlaceholdersCommand;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Injector injector;

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
                    if (queue.hasNext()) {
                        String server = queue.pop().value();
                        return Tag.preProcessParsed(proxy.getServer(server)
                                .map(sv -> sv.getPlayersConnected().size())
                                .map(size -> Integer.toString(size))
                                .orElse("0"));
                    }
                    return Tag.selfClosingInserting(Component.text(proxy.getPlayerCount()));
                })
                .globalPlaceholder("server_count", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(proxy.getAllServers().size())))
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
        injector = injector.createChildInjector(
            new CloudInjectionModule<>(
                    CommandSource.class,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            )
        );
        final VelocityCommandManager<CommandSource> commandManager = injector.getInstance(
                Key.get(new TypeLiteral<>() {})
        );
        PlaceholdersCommand.<CommandSource>builder()
                .playerSuggestions(() -> proxy.getAllPlayers()
                        .stream()
                        .map(Player::getUsername)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .toAudience(st -> proxy.getPlayer(st).orElse(null))
                .command("vminiplaceholders")
                .manager(commandManager)
                .build().register();
    }
}
