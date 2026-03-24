package io.github.miniplaceholders.minestom;

import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.command.PermissionTester;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.github.miniplaceholders.minestom.command.MinestomCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.MinecraftServer;

import java.nio.file.Path;

public class MinestomMiniPlaceholders implements PlaceholdersPlugin {
    private final ComponentLogger componentLogger = ComponentLogger.logger("miniplaceholders");
    private PermissionTester permissionTester;

    public MinestomMiniPlaceholders() {
        InternalPlatform.platform(InternalPlatform.MINESTOM);
        componentLogger.info(Component.text("Starting MiniPlaceholders Minestom", NamedTextColor.GREEN));
    }

    public void init(Path dataDirectory, PermissionTester permissionTester) {
        componentLogger.info(Component.text("Initializing MiniPlaceholders", NamedTextColor.GREEN));
        this.permissionTester = permissionTester;

        this.registerPlatformCommand();

        try {
            this.loadProvidedExpansions(dataDirectory.resolve("expansions"));
        } catch (Throwable e) {
            componentLogger.error("Unable to load expansion providers", e);
        }
    }

    /**
     * Initializes MiniPlaceholders with a default no-op permission tester.
     * This is not recommended for production use!
     */
    public void init(Path dataDirectory) {
        this.init(dataDirectory, PermissionTester.NO_OP);
    }

    @Override
    public void registerPlatformCommand() {
        MinecraftServer.getCommandManager().register(new MinestomCommand(permissionTester));
    }

    @Override
    public boolean platformHasComplementLoaded(String complementName) {
        return false; // Minestom doesn't plugins.
    }

    @Override
    public Object platformServerInstance() {
        return MinecraftServer.getServer();
    }

    @Override
    public void logInfo(Component component) {
        componentLogger.info(component);
    }

    @Override
    public void logError(Component component) {
        componentLogger.error(component);
    }
}
