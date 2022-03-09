package me.dreamerzero.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

import org.slf4j.Logger;

import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin;

@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = "1.0.0",
    authors = {"4drian3d"}
)
public class VelocityPlugin implements PlaceholdersPlugin {
    private final Logger logger;
    @Inject
    public VelocityPlugin(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholders Velocity");
    }

    @Override
    public void loadDefaultExpansions(){
        
    }
}
