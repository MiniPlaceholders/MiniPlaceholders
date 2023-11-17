package io.github.miniplaceholders.fabric;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.fabric.FabricServerCommandManager;
import io.github.miniplaceholders.common.PlaceholdersCommand;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.connect.InternalPlatform;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class FabricClientMod implements ClientModInitializer, PlaceholdersPlugin {
    @Override
    public void onInitializeClient() {
        InternalPlatform.platform(InternalPlatform.FABRIC);
        registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
    }

    @Override
    public void registerPlatformCommand() {
        final FabricServerCommandManager<CommandSourceStack> commandManager = new FabricServerCommandManager<>(
                AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
        );
        PlaceholdersCommand.<CommandSourceStack>builder()
                .hasPermissionCheck((source, permission) -> Permissions.check(source, permission, 4))
                .toAudience(string -> {
                    var server = Minecraft.getInstance().getSingleplayerServer();
                    if (server != null) {
                        return server.getPlayerList().getPlayerByName(string);
                    }
                    return Minecraft.getInstance().player;
                })
                .playerSuggestions(() -> {
                    var server = Minecraft.getInstance().getSingleplayerServer();
                    if (server != null) {
                        return new ArrayList<>(List.of(server.getPlayerList().getPlayerNamesArray()));
                    }
                    return new ArrayList<>();
                })
                .manager(commandManager)
                .command("miniplaceholders")
                .build()
                .register();
    }
}
