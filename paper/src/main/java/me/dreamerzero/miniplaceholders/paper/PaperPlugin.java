package me.dreamerzero.miniplaceholders.paper;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.tree.CommandNode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand;
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;

public class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin, Listener {
    private final CommandNode<BukkitBrigadierCommandSource> command = new PlaceholdersCommand<>(
            () -> this.getServer().getOnlinePlayers().stream().map(Player::getName).toList(),
            (String st) -> this.getServer().getPlayer(st),
            BukkitBrigadierCommandSource::getBukkitSender
        ).placeholderTestCommand("miniplaceholders");

    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
        this.setPlatform("paper");
    }

    @SuppressWarnings({"deprecation"})
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandRegister(com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent<BukkitBrigadierCommandSource> event){
        event.getCommandNode().addChild(command);
    }
}
