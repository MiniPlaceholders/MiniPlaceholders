package io.github.miniplaceholders.paper;

import io.github.miniplaceholders.common.PlaceholdersPlugin;
import io.github.miniplaceholders.common.metrics.LoadedExpansionsMetric;
import io.github.miniplaceholders.paper.listener.ServerFinishedLoadListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin implements PlaceholdersPlugin {
    @Override
    public void onEnable() {
        final ComponentLogger componentLogger = getComponentLogger();
        componentLogger.info(text("Starting MiniPlaceholders Paper", NamedTextColor.GREEN));

        new ServerFinishedLoadListener(this).register();
        final Metrics metrics = new Metrics(this, 27515);
        metrics.addCustomChart(new SingleLineChart("loaded_expansions", new LoadedExpansionsMetric()));
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

    @Override
    public Object platformServerInstance() {
        return this.getServer();
    }
}
