package io.github.miniplaceholders.paper;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.TagsUtils;
import io.github.miniplaceholders.common.PlaceholdersCommand;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.papermc.paper.datapack.Datapack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin {
    private static final DecimalFormat TPS_FORMAT = new DecimalFormat("###.##");
    private static final DecimalFormat MSPT_FORMAT = new DecimalFormat("###.###");

    static {
        InternalPlatform.platform(InternalPlatform.PAPER);
        TPS_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        MSPT_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
        this.loadDefaultExpansions();
        this.registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
            .globalPlaceholder("name", TagsUtils.staticTag(Component.text(this.getServer().getName())))
            .globalPlaceholder("online", (queue, ctx) -> TagsUtils.staticTag(Component.text(this.getServer().getOnlinePlayers().size())))
            .globalPlaceholder("version", TagsUtils.staticTag(this.getServer().getVersion()))
            .globalPlaceholder("max_players", (queue, ctx) -> TagsUtils.staticTag(Component.text(this.getServer().getMaxPlayers())))
            .globalPlaceholder("unique_joins", (queue, ctx) -> TagsUtils.staticTag(Component.text(this.getServer().getOfflinePlayers().length)))
            .globalPlaceholder("tps_1", (queue, ctx) -> TagsUtils.staticTag(TPS_FORMAT.format(this.getServer().getTPS()[0])))
            .globalPlaceholder("tps_5", (queue, ctx) -> TagsUtils.staticTag(TPS_FORMAT.format(this.getServer().getTPS()[1])))
            .globalPlaceholder("tps_15", (queue, ctx) -> TagsUtils.staticTag(TPS_FORMAT.format(this.getServer().getTPS()[2])))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> TagsUtils.staticTag(Component.text(this.getServer().hasWhitelist())))
            .globalPlaceholder("total_chunks", (queue, ctx) -> {
                int chunkCount = 0;
                for (World world : this.getServer().getWorlds()){
                    chunkCount += world.getLoadedChunks().length;
                }
                return TagsUtils.staticTag(Component.text(chunkCount));
            })
            .globalPlaceholder("total_entities", (queue, ctx) -> {
                int entityCount = 0;
                for (World world : this.getServer().getWorlds()){
                    entityCount += world.getEntityCount();
                }
                return TagsUtils.staticTag(Component.text(entityCount));
            })
            .globalPlaceholder("mspt", (queue, ctx) -> TagsUtils.staticTag(MSPT_FORMAT.format(this.getServer().getAverageTickTime())))
            .globalPlaceholder("datapack_list", (queue, ctx) -> {
                final TextComponent.Builder builder = Component.text();
                for (Datapack datapack : this.getServer().getDatapackManager().getEnabledPacks()){
                    builder.append(Component.text("[").append(Component.text(datapack.getName()).append(Component.text("] "))));
                }
                return Tag.selfClosingInserting(builder.build());
            })
            .globalPlaceholder("datapack_count", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getDatapackManager().getEnabledPacks().size())))
        .build()
        .register();
    }

    @Override
    @SuppressWarnings({"sonarlint(java:s1874)"})
    public void registerPlatformCommand() {
        try {
            PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(
                    this,
                    AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
            commandManager.registerBrigadier();

            PlaceholdersCommand.<CommandSender>builder()
                    .playerSuggestions(() -> getServer().getOnlinePlayers()
                            .stream()
                            .map(Player::getName)
                            .collect(Collectors.toCollection(ArrayList::new)))
                    .toAudience(st -> getServer().getPlayer(st))
                    .hasPermissionCheck(Permissible::hasPermission)
                    .manager(commandManager)
                    .command("miniplaceholders")
                    .build()
                    .register();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
