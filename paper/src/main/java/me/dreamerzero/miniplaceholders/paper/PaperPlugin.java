package me.dreamerzero.miniplaceholders.paper;

import org.bukkit.plugin.java.JavaPlugin;

import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;

public class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin {
    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting MiniPlaceholders Paper");
    }

    @Override
    public void loadDefaultExpansions(){
        
    }
}
