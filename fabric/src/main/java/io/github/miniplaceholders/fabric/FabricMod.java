package io.github.miniplaceholders.fabric;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersCommand;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.connect.InternalPlatform;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FabricMod implements ModInitializer, PlaceholdersPlugin {
    private final Logger logger = LoggerFactory.getLogger("miniplaceholders");
    private MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        logger.info("Starting MiniPlaceholders Fabric");
        InternalPlatform.platform(InternalPlatform.FABRIC);

        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> this.minecraftServer = minecraftServer);

        this.loadDefaultExpansions();
        this.registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
                .globalPlaceholder("name", Tag.selfClosingInserting(Component.text("Minecraft")))
                .globalPlaceholder("version", (ctx, queue) -> Tag.selfClosingInserting(Component.text(this.minecraftServer.getServerVersion())))
                .globalPlaceholder("max_players", (ctx, queue) -> Tag.selfClosingInserting(Component.text(this.minecraftServer.getMaxPlayers())))
                .globalPlaceholder("tps", (ctx, queue) -> Tag.selfClosingInserting(Component.text(this.minecraftServer.getTickCount())))
                .build()
                .register();
    }

    @Override
    public void registerPlatformCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, commandSelection) ->
            dispatcher.register(PlaceholdersCommand.<CommandSourceStack>builder()
                    .hasPermissionCheck((source, permission) -> Permissions.check(source, permission, 4))
                    .toAudience(string -> this.minecraftServer.getPlayerList().getPlayerByName(string))
                    .playerSuggestions(() -> Arrays.asList(this.minecraftServer.getPlayerNames()))
                    .build()
                    .asBuilder("miniplaceholders"))
        );
    }
}
