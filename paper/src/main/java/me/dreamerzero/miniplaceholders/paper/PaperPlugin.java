package me.dreamerzero.miniplaceholders.paper;

import org.bukkit.plugin.java.JavaPlugin;

public class PaperPlugin extends JavaPlugin {
    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
    }
}
