package io.github.miniplaceholders.minestom;

import io.github.miniplaceholders.minestom.listener.AsyncPlayerConfigurationListener;
import net.kyori.adventure.util.TriState;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.lan.OpenToLAN;

import java.nio.file.Path;

public class MinestomServer {

    static void main() {
        MinecraftServer server = MinecraftServer.init(new Auth.Online());

        MinecraftServer.getGlobalEventHandler().addListener(new AsyncPlayerConfigurationListener());

        MinestomMiniPlaceholders.initialize(
            Path.of("miniplaceholders"),
            (_, _) -> TriState.TRUE
        );

        OpenToLAN.open();
        server.start("0.0.0.0", 25565);
    }

}
