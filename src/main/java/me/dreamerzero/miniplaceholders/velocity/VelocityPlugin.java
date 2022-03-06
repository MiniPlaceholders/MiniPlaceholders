package me.dreamerzero.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = "1.0.0",
    authors = {"4drian3d"}
)
@ApiStatus.Internal
public class VelocityPlugin {
    private final Logger logger;
    @Inject
    @ApiStatus.Internal
    public VelocityPlugin(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    @ApiStatus.Internal
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholders Velocity");
    }
}
