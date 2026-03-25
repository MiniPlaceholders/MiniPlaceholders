package io.github.miniplaceholders.minestom.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jspecify.annotations.NonNull;

public class AsyncPlayerConfigurationListener implements EventListener<AsyncPlayerConfigurationEvent> {

    private final Instance instance;

    public AsyncPlayerConfigurationListener() {
        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 32, Block.GRASS_BLOCK));
    }

    @Override
    public @NonNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NonNull Result run(AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();

        event.setSpawningInstance(instance);
        player.setRespawnPoint(new Pos(0, 33, 0));
        return Result.SUCCESS;
    }
}
