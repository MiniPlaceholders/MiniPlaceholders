package me.dreamerzero.miniplaceholders.paper;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.NumberFormat;

import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.datapack.Datapack;
import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;
import me.dreamerzero.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.minecraft.commands.CommandSourceStack;

public final class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin, Listener {
    private final NumberFormat tpsFormat = NumberFormat.getInstance();
    private final NumberFormat msptFormat = NumberFormat.getInstance();

    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
        InternalPlatform.platform(InternalPlatform.PAPER);
        this.getServer().getPluginManager().registerEvents(this, this);

        tpsFormat.setRoundingMode(RoundingMode.DOWN);
        tpsFormat.setMaximumFractionDigits(2);

        msptFormat.setRoundingMode(RoundingMode.DOWN);
        msptFormat.setMaximumFractionDigits(3);
        
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
            .globalPlaceholder("tps_1", (queue, ctx) -> TagsUtils.staticTag(tpsFormat.format(this.getServer().getTPS()[0])))
            .globalPlaceholder("tps_5", (queue, ctx) -> TagsUtils.staticTag(tpsFormat.format(this.getServer().getTPS()[1])))
            .globalPlaceholder("tps_15", (queue, ctx) -> TagsUtils.staticTag(tpsFormat.format(this.getServer().getTPS()[2])))
            .globalPlaceholder("has_whitelist", (queue, ctx) -> TagsUtils.staticTag(Component.text(this.getServer().hasWhitelist())))
            .globalPlaceholder("total_chunks", (queue, ctx) -> {
                int chunkCount = 0;
                for(World world : this.getServer().getWorlds()){
                    chunkCount += world.getLoadedChunks().length;
                }
                return TagsUtils.staticTag(Component.text(chunkCount));
            })
            .globalPlaceholder("total_entities", (queue, ctx) -> {
                int entityCount = 0;
                for(World world : this.getServer().getWorlds()){
                    entityCount += world.getEntityCount();
                }
                return TagsUtils.staticTag(Component.text(entityCount));
            })
            .globalPlaceholder("mspt", (queue, ctx) -> TagsUtils.staticTag(msptFormat.format(this.getServer().getAverageTickTime())))
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
        this.getNMSServer()
            .vanillaCommandDispatcher
            .getDispatcher()
            .register(new PlaceholdersCommand<>(
                    () -> this.getServer().getOnlinePlayers().stream().map(Player::getName).toList(),
                    (String st) -> this.getServer().getPlayer(st),
                    CommandSourceStack::getBukkitSender
                ).placeholderTestBuilder("miniplaceholders")
            );
    }

    private DedicatedServer getNMSServer(){
        Server craftServer = this.getServer();
        try {
            return (DedicatedServer) craftServer.getClass().getDeclaredMethod("getServer").invoke(craftServer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException("Your Minecraft version is not supported by MiniPlaceholders", exception);
        }
    }
}
