package io.github.miniplaceholders.paper.listener;

import io.github.miniplaceholders.paper.PaperPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public record ServerFinishedLoadListener(PaperPlugin plugin) implements Listener, EventExecutor {

  public void register() {
    plugin.getServer()
        .getPluginManager()
        .registerEvent(ServerLoadEvent.class, this, EventPriority.HIGH, this, plugin);
  }


  @Override
  public void execute(final @NotNull Listener listener, final @NotNull Event event) {
    final ServerLoadEvent loadEvent = (ServerLoadEvent) event;
    if (loadEvent.getType() != ServerLoadEvent.LoadType.STARTUP) {
      return;
    }

    try {
      plugin.loadProvidedExpansions(plugin.getDataPath().resolve("expansions"));
    } catch (Throwable e) {
      plugin.getComponentLogger().error("Unable to load expansion providers", e);
    }
  }
}
