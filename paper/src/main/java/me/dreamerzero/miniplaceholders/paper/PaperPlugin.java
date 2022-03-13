package me.dreamerzero.miniplaceholders.paper;

import java.math.RoundingMode;
import java.text.NumberFormat;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.datapack.Datapack;
import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.api.enums.Platform;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.minecraft.commands.CommandSourceStack;

public final class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin, Listener {
    private final NumberFormat tpsFormat = NumberFormat.getInstance();
    private final NumberFormat msptFormat = NumberFormat.getInstance();
    {
        tpsFormat.setRoundingMode(RoundingMode.DOWN);
        tpsFormat.setMaximumFractionDigits(2);

        msptFormat.setRoundingMode(RoundingMode.DOWN);
        msptFormat.setMaximumFractionDigits(3);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
        MiniPlaceholders.setPlatform(Platform.PAPER);
        this.getServer().getPluginManager().registerEvents(this, this);

        this.loadDefaultExpansions();
        this.registerPlatformCommand();
    }

    @Override
    public void loadDefaultExpansions() {
        Expansion.builder("server")
            .globalPlaceholder("name", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getName())))
            .globalPlaceholder("online", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getOnlinePlayers().size())))
            .globalPlaceholder("version", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getVersion())))
            .globalPlaceholder("max_players", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getMaxPlayers())))
            .globalPlaceholder("unique_joins", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getOfflinePlayers().length)))
            .globalPlaceholder("tps_1", (queue, ctx) -> Tag.selfClosingInserting(Component.text(tpsFormat.format(this.getCraftServer().getHandle().getServer().recentTps[0]))))
            .globalPlaceholder("tps_5", (queue, ctx) -> Tag.selfClosingInserting(Component.text(tpsFormat.format(this.getCraftServer().getHandle().getServer().recentTps[1]))))
            .globalPlaceholder("tps_15", (queue, ctx) -> Tag.selfClosingInserting(Component.text(tpsFormat.format(this.getCraftServer().getHandle().getServer().recentTps[2]))))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().hasWhitelist())))
            .globalPlaceholder("total_chunks", (queue, ctx) -> {
                int chunkCount = 0;
                for(World world : this.getServer().getWorlds()){
                    chunkCount += world.getLoadedChunks().length;
                }
                return Tag.selfClosingInserting(Component.text(chunkCount));
            })
            .globalPlaceholder("total_entities", (queue, ctx) -> {
                int entityCount = 0;
                for(World world : this.getServer().getWorlds()){
                    entityCount += world.getEntityCount();
                }
                return Tag.selfClosingInserting(Component.text(entityCount));
            })
            .globalPlaceholder("mspt", (queue, ctx) -> Tag.selfClosingInserting(Component.text(msptFormat.format(this.getServer().getAverageTickTime()))))
            .globalPlaceholder("datapack_list", (queue, ctx) -> {
                TextComponent.Builder builder = Component.text();
                for(Datapack datapack : this.getServer().getDatapackManager().getEnabledPacks()){
                    builder.append(Component.text("[").append(Component.text(datapack.getName()).append(Component.text("] "))));
                }
                return Tag.selfClosingInserting(builder.build());
            })
            .globalPlaceholder("datapack_count", (queue, ctx) -> Tag.selfClosingInserting(Component.text(this.getServer().getDatapackManager().getEnabledPacks().size())))
        .build()
        .register();
    }

    @Override
    @SuppressWarnings("all")
    public void registerPlatformCommand() {
        this.getCraftServer().getServer()
            .vanillaCommandDispatcher
            .getDispatcher()
            .register(new PlaceholdersCommand<>(
                    () -> this.getServer().getOnlinePlayers().stream().map(Player::getName).toList(),
                    (String st) -> this.getServer().getPlayer(st),
                    CommandSourceStack::getBukkitSender
                ).placeholderTestBuilder("miniplaceholders")
            );
    }

    private CraftServer getCraftServer(){
        return (CraftServer)this.getServer();
    }
}
