package me.dreamerzero.miniplaceholders.krypton;

import org.apache.logging.log4j.Logger;
import org.kryptonmc.api.Server;
import org.kryptonmc.api.command.BrigadierCommand;
import org.kryptonmc.api.command.Sender;
import org.kryptonmc.api.entity.player.Player;
import org.kryptonmc.api.event.Listener;
import org.kryptonmc.api.event.server.ServerStartEvent;
import org.kryptonmc.api.plugin.annotation.Plugin;
//import org.kryptonmc.krypton.KryptonServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import com.google.inject.Inject;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.common.PluginConstants;
import me.dreamerzero.miniplaceholders.connect.InternalPlatform;

/*@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = PluginConstants.VERSION,
    authors = {"4drian3d"}
)*/
public class KryptonPlugin implements PlaceholdersPlugin {
    private final Server server;
    private final Logger logger;

    @Inject
    public KryptonPlugin(Server server, Logger logger) {
        this.logger = logger;
        this.server = server;
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
                for(var entry : server.worldManager().worlds().entrySet()) {
                    entityCount += entry.getValue().entities().size();
                }
                return TagsUtils.staticTag(Component.text(entityCount));
            })
            .globalPlaceholder("total_chunks", (queque, ctx) -> {
                int chunkCount = 0;
                for(var entry : server.worldManager().worlds().entrySet()) {
                    chunkCount += entry.getValue().chunks().size();
                }
                return TagsUtils.staticTag(Component.text(chunkCount));
            })
            .globalPlaceholder("name", TagsUtils.staticTag(server.platform().name()))
            
            .globalPlaceholder("online", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.players().size())))
            .globalPlaceholder("max_players", TagsUtils.staticTag(Component.text(server.maxPlayers())))
            //.globalPlaceholder("has_whitelist", (queue, ctx) -> TagsUtils.staticTag(Component.text(server.playerManager().whitelistEnabled())))
        .build()
        .register();
        
    }

    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    @Override
    public void registerPlatformCommand() {
        final PlaceholdersCommand<Sender> command = new PlaceholdersCommand<Sender>(
            () -> server.players()
                .stream()
                .map(player -> SERIALIZER.serialize(player.name()))
                .toList(),
            st -> server.player(st));
        final BrigadierCommand brigadierCMD = BrigadierCommand.of(command.placeholderTestCommand("miniplaceholders"));

        server.commandManager().register(brigadierCMD);
    }
}
