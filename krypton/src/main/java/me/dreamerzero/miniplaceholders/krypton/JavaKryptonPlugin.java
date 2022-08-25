package me.dreamerzero.miniplaceholders.krypton;

import org.apache.logging.log4j.Logger;
import org.kryptonmc.api.Server;
import org.kryptonmc.api.command.BrigadierCommand;
import org.kryptonmc.api.command.Sender;
import org.kryptonmc.api.entity.player.Player;
import org.kryptonmc.api.event.Listener;
import org.kryptonmc.api.event.server.ServerStartEvent;
import org.kryptonmc.api.plugin.annotation.Plugin;
import org.kryptonmc.krypton.KryptonServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import com.google.inject.Inject;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.common.PluginConstants;
import me.dreamerzero.miniplaceholders.connect.InternalPlatform;
import me.lucko.spark.api.statistic.StatisticWindow;

@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = PluginConstants.VERSION,
    authors = {"4drian3d"}
)
public class JavaKryptonPlugin implements PlaceholdersPlugin {
    private final KryptonServer server;
    private final Logger logger;

    @Inject
    public JavaKryptonPlugin(Server server, Logger logger) {
        this.logger = logger;
        this.server = (KryptonServer)server;
    }

    @Listener
    public void onServerStart(ServerStartEvent event) {
        logger.info("Starting MiniPlaceholders Krypton");

        InternalPlatform.platform(InternalPlatform.KRYPTON);

        this.loadDefaultExpansions();
        this.registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
            .globalPlaceholder("total_entities", (queue, ctx) -> {
                int entityCount = 0;
                for (var entry : server.getWorldManager().getWorlds().entrySet()) {
                    entityCount += entry.getValue().entities().size();
                }
                return TagsUtils.staticTag(Component.text(entityCount));
            })
            .globalPlaceholder("total_chunks", (queque, ctx) -> {
                int chunkCount = 0;
                for(var entry : server.getWorldManager().getWorlds().entrySet()) {
                    chunkCount += entry.getValue().chunks().size();
                }
                return TagsUtils.staticTag(Component.text(chunkCount));
            })
            .globalPlaceholder("name", TagsUtils.staticTag(server.getPlatform().getName()))
            .globalPlaceholder("online", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getPlayers().size())))
            .globalPlaceholder("version", TagsUtils.staticTag(server.getPlatform().getVersion()))
            .globalPlaceholder("max_players", TagsUtils.staticTag(Component.text(server.getMaxPlayers())))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getPlayerManager().getWhitelistEnabled())))
            .globalPlaceholder("tps_1", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getSpark().tps().poll(StatisticWindow.TicksPerSecond.MINUTES_1))))
            .globalPlaceholder("tps_5", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getSpark().tps().poll(StatisticWindow.TicksPerSecond.MINUTES_5))))
            .globalPlaceholder("tps_15", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getSpark().tps().poll(StatisticWindow.TicksPerSecond.MINUTES_15))))
            .globalPlaceholder("mspt", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.getSpark().mspt().poll(StatisticWindow.MillisPerTick.SECONDS_10).mean())))
        .build()
        .register();
    }

    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    @Override
    public void registerPlatformCommand() {
        final PlaceholdersCommand<Sender> command = new PlaceholdersCommand<>(
            () -> server.getPlayers()
                .stream()
                .map(Player::name)
                .map(SERIALIZER::serialize)
                .toList(),
            server::player);
        final BrigadierCommand brigadierCMD = BrigadierCommand.of(command.placeholderTestCommand("miniplaceholders"));

        server.getCommandManager().register(brigadierCMD);
    }
}
