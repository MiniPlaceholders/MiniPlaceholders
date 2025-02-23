package io.github.miniplaceholders.paper;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.papermc.paper.datapack.Datapack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
            .version(PluginConstants.VERSION)
            .author("MiniPlaceholders Contributors")
            .globalPlaceholder("name", Tag.preProcessParsed(this.getServer().getName()))
            .globalPlaceholder("online", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.getServer().getOnlinePlayers().size())))
            .globalPlaceholder("version", Tag.preProcessParsed(this.getServer().getVersion()))
            .globalPlaceholder("max_players", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.getServer().getMaxPlayers())))
            .globalPlaceholder("unique_joins", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.getServer().getOfflinePlayers().length)))
            .globalPlaceholder("tps_1", (queue, ctx) -> Tag.preProcessParsed(TPS_FORMAT.format(this.getServer().getTPS()[0])))
            .globalPlaceholder("tps_5", (queue, ctx) -> Tag.preProcessParsed(TPS_FORMAT.format(this.getServer().getTPS()[1])))
            .globalPlaceholder("tps_15", (queue, ctx) -> Tag.preProcessParsed(TPS_FORMAT.format(this.getServer().getTPS()[2])))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> Tag.preProcessParsed(Boolean.toString(this.getServer().hasWhitelist())))
            .globalPlaceholder("total_chunks", (queue, ctx) -> {
                int chunkCount = 0;
                for (World world : this.getServer().getWorlds()){
                    chunkCount += world.getLoadedChunks().length;
                }
                return Tag.preProcessParsed(Integer.toString(chunkCount));
            })
            .globalPlaceholder("total_entities", (queue, ctx) -> {
                int entityCount = 0;
                for (World world : this.getServer().getWorlds()){
                    entityCount += world.getEntityCount();
                }
                return Tag.preProcessParsed(Integer.toString(entityCount));
            })
            .globalPlaceholder("mspt", (queue, ctx) -> Tag.preProcessParsed(MSPT_FORMAT.format(this.getServer().getAverageTickTime())))
            .globalPlaceholder("datapack_list", (queue, ctx) -> {
                final TextComponent.Builder builder = Component.text();
                for (final Datapack datapack : this.getServer().getDatapackManager().getEnabledPacks()) {
                    builder.append(Component.text("[").append(Component.text(datapack.getName()).append(Component.text("] "))));
                }
                return Tag.selfClosingInserting(builder.build());
            })
            .globalPlaceholder("datapack_count", (queue, ctx) -> Tag.preProcessParsed(Integer.toString(this.getServer().getDatapackManager().getEnabledPacks().size())))
        .build()
        .register();
    }

    @Override
    public void registerPlatformCommand() {
    }
}
