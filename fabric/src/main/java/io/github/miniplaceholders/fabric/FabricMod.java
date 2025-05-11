package io.github.miniplaceholders.fabric;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.connect.InternalPlatform;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.fabric.FabricServerCommandManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;

public class FabricMod implements ModInitializer, PlaceholdersPlugin {
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
        final List<Expansion> loadedExpansions = this.loadProvidedExpansions(expansionsFolder);
        if (loadedExpansions.isEmpty()) {
          componentLogger.info(text("Not found expansions", NamedTextColor.GRAY));
        } else {
          final String expansionsInfo = loadedExpansions.stream()
                  .map(Expansion::shortToString)
                  .collect(Collectors.joining(","));
          componentLogger.info(text("Loaded expansions: " + expansionsInfo, NamedTextColor.GREEN));
        }
      } catch (Throwable e) {
        componentLogger.error("Unable to load expansion providers", e);
      }
    });

    this.registerPlatformCommand();
  }

  @Override
  public void registerPlatformCommand() {
    final FabricServerCommandManager<CommandSourceStack> commandManager = new FabricServerCommandManager<>(
            ExecutionCoordinator.simpleCoordinator(),
            SenderMapper.identity()
    );
    PlaceholdersCommand.<CommandSourceStack>builder()
            .hasPermissionCheck((source, permission) -> Permissions.check(source, permission, 4))
            .toAudience(string -> this.minecraftServer.getPlayerList().getPlayerByName(string))
            .playerSuggestions(() -> new ArrayList<>(List.of(this.minecraftServer.getPlayerNames())))
            .manager(commandManager)
            .command("miniplaceholders")
            .build()
            .register();
  }

  @Override
  public boolean platformHasComplementLoaded(String complementName) {
    return FabricLoader.getInstance().isModLoaded(complementName);
  }
}
