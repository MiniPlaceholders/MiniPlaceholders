package io.github.miniplaceholders.fabric;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.command.BrigadierCommandProvider;
import io.github.miniplaceholders.connect.InternalPlatform;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FabricMod implements ModInitializer, PlaceholdersPlugin {
  private final ComponentLogger componentLogger = ComponentLogger.logger("miniplaceholders");
  private MinecraftServer minecraftServer;

  @Override
  public void onInitialize() {
    componentLogger.info("Starting MiniPlaceholders Fabric");
    InternalPlatform.platform(InternalPlatform.FABRIC);

    ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
      this.minecraftServer = minecraftServer;
      try {
        final Path expansionsFolder = FabricLoader.getInstance().getConfigDir()
            .resolve("miniplaceholders").resolve("expansions");
        this.loadProvidedExpansions(expansionsFolder);
      } catch (Throwable e) {
        componentLogger.error("Unable to load expansion providers", e);
      }
    });

    this.registerPlatformCommand();
  }

  @Override
  public void registerPlatformCommand() {
    final LiteralCommandNode<CommandSourceStack> node = BrigadierCommandProvider.provideCommand(
        "miniplaceholders",
        () -> new ArrayList<>(List.of(this.minecraftServer.getPlayerNames())),
        string -> this.minecraftServer.getPlayerList().getPlayerByName(string),
        aud -> aud,
        (audience, permission) -> {
          final CommandSourceStack commandSourceStack = (CommandSourceStack) audience;
          return switch (Permissions.getPermissionValue(commandSourceStack, permission)) {
            case TRUE -> TriState.TRUE;
            case FALSE -> TriState.FALSE;
            case DEFAULT -> commandSourceStack.hasPermission(3) ? TriState.TRUE : TriState.NOT_SET;
          };
        }
    );
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, ctx, selection) ->
            dispatcher.getRoot().addChild(node)
    );
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return FabricLoader.getInstance().isModLoaded(complementName);
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
    return this.minecraftServer;
  }
}
