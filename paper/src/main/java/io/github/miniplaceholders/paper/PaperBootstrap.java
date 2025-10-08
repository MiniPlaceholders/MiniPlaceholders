package io.github.miniplaceholders.paper;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.miniplaceholders.common.command.brigadier.BrigadierCommandProvider;
import io.github.miniplaceholders.connect.InternalPlatform;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "UnstableApiUsage"})
@NullMarked
public final class PaperBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(final BootstrapContext bootstrapContext) {
        InternalPlatform.platform(InternalPlatform.PAPER);
        final LiteralCommandNode<CommandSourceStack> command = BrigadierCommandProvider.provideCommand(
            "miniplaceholders",
            () -> Bukkit.getServer()
                .getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toCollection(ArrayList::new)),
            string -> Bukkit.getServer().getPlayer(string),
            CommandSourceStack::getSender,
            (audience, permission) -> {
              final CommandSender sender = (CommandSender) audience;
              final TriState triState = sender.permissionValue(permission);
              return triState == TriState.NOT_SET
                  ? sender.isOp() ? TriState.TRUE : TriState.NOT_SET
                  : triState;
            }
        );
        bootstrapContext.getLifecycleManager()
            .registerEventHandler(LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(bootstrapContext.getPluginMeta(), command, "", List.of()));

    }
}
