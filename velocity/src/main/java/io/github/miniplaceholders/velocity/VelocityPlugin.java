package io.github.miniplaceholders.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.CloudInjectionModule;
import org.incendo.cloud.velocity.VelocityCommandManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Plugin(
        name = "MiniPlaceholders",
        id = "miniplaceholders",
        version = PluginConstants.VERSION,
        authors = {"MiniPlaceholders Contributors", "4drian3d"},
        description = "MiniMessage Component-based Placeholders API"
)
public final class VelocityPlugin implements PlaceholdersPlugin {
    @Inject
    private Injector injector;
    @Inject
    @DataDirectory
    private Path dataDirectory;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private ComponentLogger componentLogger;

    @Subscribe
    public void onProxyInitialize(final ProxyInitializeEvent event) {
        componentLogger.info(Component.text("Starting MiniPlaceholders Velocity", NamedTextColor.GREEN));

        InternalPlatform.platform(InternalPlatform.VELOCITY);

        this.registerPlatformCommand();

        try {
            this.loadProvidedExpansions(dataDirectory.resolve("expansions"));
        } catch (Throwable e) {
            componentLogger.error("Unable to load expansion providers", e);
        }
    }

    @Override
    public void registerPlatformCommand() {
        injector = injector.createChildInjector(
                new CloudInjectionModule<>(
                        CommandSource.class,
                        ExecutionCoordinator.simpleCoordinator(),
                        SenderMapper.identity()
                )
        );
        final VelocityCommandManager<CommandSource> commandManager = injector.getInstance(
                Key.get(new TypeLiteral<>() {
                })
        );
        PlaceholdersCommand.<CommandSource>builder()
                .playerSuggestions(() -> proxyServer.getAllPlayers()
                        .stream()
                        .map(Player::getUsername)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .toAudience(st -> proxyServer.getPlayer(st).orElse(null))
                .command("vminiplaceholders")
                .manager(commandManager)
                .build().register();
    }

    @Override
    public boolean platformHasComplementLoaded(String complementName) {
        return this.proxyServer.getPluginManager().isLoaded(complementName);
    }

    @Override
    public void logError(Component component) {
        componentLogger.error(component);
    }

    @Override
    public void logInfo(Component component) {
        componentLogger.info(component);
    }
}
