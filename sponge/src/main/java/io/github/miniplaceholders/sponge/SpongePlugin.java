package io.github.miniplaceholders.sponge;

import com.google.inject.Inject;
import io.github.miniplaceholders.api.Expansion;
//import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.connect.InternalPlatform;
//import net.kyori.adventure.audience.Audience;
//import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.apache.logging.log4j.Logger;
//import org.incendo.cloud.sponge7.SpongeCommandManager;
//import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
//import org.spongepowered.api.command.CommandCause;
//import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.world.server.ServerWorld;
//import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

//import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Collectors;

@Plugin("miniplaceholders")
public class SpongePlugin implements PlaceholdersPlugin {

//    @Inject
//    private PluginContainer pluginContainer;
    @Inject
    private Logger logger;

    private Server server;

    @Listener
    public void onServerStart(final StartingEngineEvent<Server> event) {
        this.server = event.engine();
        logger.info("Starting MiniPlaceholders Sponge");
        InternalPlatform.platform(InternalPlatform.SPONGE);

        this.loadDefaultExpansions();
    }

    private final AtomicInteger registration = new AtomicInteger(0);

    @Listener
    public void onCommandRegister(final RegisterCommandEvent<Command.Parameterized> event) {
        if (registration.getAndIncrement() == 0) {
            this.registerPlatformCommand();
        }
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
                .version(PluginConstants.VERSION)
                .author("MiniPlaceholders Contributors")
                .globalPlaceholder("name", Tag.preProcessParsed("Sponge"))
                .globalPlaceholder("online", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(server.onlinePlayers().size())))
                .globalPlaceholder("max_players", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(server.maxPlayers())))
                .globalPlaceholder("unique_joins", (queue, ctx) -> Tag.preProcessParsed(Long.toString(server.userManager().streamAll().count())))
                .globalPlaceholder("has_whitelist", (queue, ctx) -> Tag.preProcessParsed(Boolean.toString(server.isWhitelistEnabled())))
                .globalPlaceholder("total_chunks", (queue, ctx) -> {
                    int chunkCount = 0;
                    for (ServerWorld world : server.worldManager().worlds()){
                        chunkCount += world.entities().size();
                    }
                    return Tag.preProcessParsed(Integer.toString(chunkCount));
                })
                .globalPlaceholder("total_entities", (queue, ctx) -> {
                    int entityCount = 0;
                    for (ServerWorld world : server.worldManager().worlds()){
                        entityCount += world.entities().size();
                    }
                    return Tag.preProcessParsed(Integer.toString(entityCount));
                })
                .globalPlaceholder("mspt", (queue, ctx) -> Tag.preProcessParsed(Double.toString(server.averageTickTime())))
                .build()
                .register();

    }

    @Override
    public void registerPlatformCommand() {
//        Cloud 2 doesn't support Sponge 8 yet?
//        final SpongeCommandManager<AudienceHolder> commandManager = null;
//
//        PlaceholdersCommand.<AudienceHolder>builder()
//                .manager(commandManager)
//                .command("miniplaceholders")
//                .playerSuggestions(() -> server.onlinePlayers()
//                        .stream()
//                        .map(ServerPlayer::name)
//                        .collect(Collectors.toCollection(ArrayList::new)))
//                .toAudience(st -> server.player(st).orElse(null))
//                .hasPermissionCheck((holder, permission) -> holder.cause.hasPermission(permission))
//                .build()
//                .register();
    }

//    record AudienceHolder(@NotNull CommandCause cause) implements ForwardingAudience.Single {
//
//        @Override
//        public @NotNull Audience audience() {
//            return cause.audience();
//        }
//    }
}
