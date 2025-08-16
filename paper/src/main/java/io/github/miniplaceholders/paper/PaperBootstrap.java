package io.github.miniplaceholders.paper;

import io.github.miniplaceholders.common.command.PlaceholdersCommand;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class PaperBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        InternalPlatform.platform(InternalPlatform.PAPER);
        PaperCommandManager<SourceWrapper> commandManager = PaperCommandManager
                .builder(SenderMapper.create(SourceWrapper::new, SourceWrapper::stack))
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildBootstrapped(bootstrapContext);

        PlaceholdersCommand.<SourceWrapper>builder()
                .playerSuggestions(() -> Bukkit.getServer().getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .toAudience(st -> Bukkit.getServer().getPlayer(st))
                .hasPermissionCheck((aud, p) -> aud.stack.getSender().hasPermission(p))
                .manager(commandManager)
                .command("miniplaceholders")
                .build()
                .register();

    }

    public record SourceWrapper(CommandSourceStack stack) implements ForwardingAudience.Single {

        @Override
        public @NotNull Audience audience() {
            return this.stack.getSender();
        }
    }
}
