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

public final class MinestomMiniPlaceholders implements PlaceholdersPlugin {
    private static final ComponentLogger LOGGER = ComponentLogger.logger("miniplaceholders");
    private final PermissionTester permissionTester;

    private MinestomMiniPlaceholders(PermissionTester permissionTester) {
        this.permissionTester = permissionTester;
    }

    public static MinestomMiniPlaceholders initialize(Path dataDirectory, PermissionTester permissionTester) {
        InternalPlatform.platform(InternalPlatform.MINESTOM);
        LOGGER.info(Component.text("Initializing MiniPlaceholders", NamedTextColor.GREEN));
        final MinestomMiniPlaceholders miniPlaceholders = new MinestomMiniPlaceholders(permissionTester);
        miniPlaceholders.registerPlatformCommand();

        try {
            miniPlaceholders.loadProvidedExpansions(dataDirectory.resolve("expansions"));
        } catch (Throwable e) {
            LOGGER.error("Unable to load expansion providers", e);
        }

        return miniPlaceholders;
    }

    /**
     * Initializes MiniPlaceholders with a default no-op permission tester.
     * This is not recommended for production use!
     */
    public static MinestomMiniPlaceholders initialize(final Path dataDirectory) {
        return MinestomMiniPlaceholders.initialize(dataDirectory, PermissionTester.NO_OP);
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
        LOGGER.info(component);
    }

    @Override
    public void logError(Component component) {
        LOGGER.error(component);
    }
}
