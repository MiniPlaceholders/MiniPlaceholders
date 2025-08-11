package io.github.miniplaceholders.paper;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.common.PlaceholdersPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin {
    @Override
    public void onEnable() {
        final ComponentLogger componentLogger = getComponentLogger();
        componentLogger.info(text("Starting MiniPlaceholders Paper", NamedTextColor.GREEN));

        try {
            this.loadProvidedExpansions(getDataPath().resolve("expansions"));
        } catch (Throwable e) {
            componentLogger.error("Unable to load expansion providers", e);
        }
    }

    @Override
    public boolean platformHasComplementLoaded(String complementName) {
        return getServer().getPluginManager().isPluginEnabled(complementName);
    }

    @Override
    public void logError(Component component) {
        getComponentLogger().error(component);
    }

    @Override
    public void logInfo(Component component) {
        getComponentLogger().info(component);
    }
}
