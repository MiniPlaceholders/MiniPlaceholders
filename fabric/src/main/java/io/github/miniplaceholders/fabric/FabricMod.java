package io.github.miniplaceholders.fabric;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.connect.InternalPlatform;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackResources;
import net.minecraft.world.entity.Entity;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.fabric.FabricServerCommandManager;

import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;

public class FabricMod implements ModInitializer, PlaceholdersPlugin {
  private final ComponentLogger componentLogger = ComponentLogger.logger("miniplaceholders");
  private static final DecimalFormat MSPT_FORMAT = new DecimalFormat("###.###");

  private final TickManager tickManager = new TickManager();
  private MinecraftServer minecraftServer;

  @Override
  public void onInitialize() {
    componentLogger.info("Starting MiniPlaceholders Fabric");
    InternalPlatform.platform(InternalPlatform.FABRIC);

    ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
      this.minecraftServer = minecraftServer;
      this.loadDefaultExpansions();
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
  public void loadDefaultExpansions() {
    Expansion.builder("server")
            .author("MiniPlaceholders Contributors")
            .version(PluginConstants.VERSION)
            .globalPlaceholder("name", Tag.preProcessParsed(this.minecraftServer.getServerModName()))
            .globalPlaceholder("online", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.minecraftServer.getPlayerCount())))
            .globalPlaceholder("version", (ctx, queue) -> Tag.preProcessParsed(this.minecraftServer.getServerVersion()))
            .globalPlaceholder("max_players", (ctx, queue) -> Tag.preProcessParsed(Integer.toString(this.minecraftServer.getMaxPlayers())))
            .globalPlaceholder("unique_joins", (queue, ctx) -> {
              final var profileCache = this.minecraftServer.getProfileCache();
              if (profileCache == null) {
                return Tag.preProcessParsed("0");
              }
              return Tag.preProcessParsed(Integer.toString(profileCache.load().size()));
            })
            .globalPlaceholder("tps_1m", (ctx, queue) -> Tag.preProcessParsed(tickManager.getTps1m().formattedAverage()))
            .globalPlaceholder("tps_5m", (ctx, queue) -> Tag.preProcessParsed(tickManager.getTps5m().formattedAverage()))
            //.globalPlaceholder("tps_15m", (ctx, queue) -> Tag.preProcessParsed(tickManager.getTps15m().formattedAverage()))
            .globalPlaceholder("tick_count", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.minecraftServer.getTickCount())))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> Tag.preProcessParsed(Boolean.toString(this.minecraftServer.getPlayerList().isUsingWhitelist())))
            .globalPlaceholder("total_chunks", (queue, ctx) -> {
              int chunks = 0;
              for (final ServerLevel level : this.minecraftServer.getAllLevels()) {
                chunks += level.getChunkSource().getLoadedChunksCount();
              }
              return Tag.preProcessParsed(Integer.toString(chunks));
            })
            .globalPlaceholder("total_entities", (queue, ctx) -> {
              int entities = 0;
              for (final ServerLevel level : this.minecraftServer.getAllLevels()) {
                for (final Entity ignored : level.getAllEntities()) {
                  entities++;
                }
              }
              return Tag.preProcessParsed(Integer.toString(entities));
            })
            .globalPlaceholder("mspt", (queue, ctx) -> Tag.preProcessParsed(MSPT_FORMAT.format(this.minecraftServer.getAverageTickTimeNanos())))
            .globalPlaceholder("datapack_list", (queue, ctx) ->
                    Tag.selfClosingInserting(this.minecraftServer.getResourceManager()
                            .listPacks()
                            .map(PackResources::packId)
                            .map(id -> Component.text()
                                    .content("[")
                                    .append(Component.text(id))
                                    .append(Component.text("]"))
                                    .build())
                            .collect(Component.toComponent(Component.space())))
            )
            .globalPlaceholder("datapack_count", (queue, ctx) ->
                    Tag.preProcessParsed(Long.toString(this.minecraftServer.getResourceManager()
                            .listPacks()
                            .count()))
            )
            .build()
            .register();
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
