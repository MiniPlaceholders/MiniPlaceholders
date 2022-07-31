package me.dreamerzero.miniplaceholders.krypton

import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.plugin.annotation.Plugin
//import org.kryptonmc.krypton.KryptonServer

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

import com.google.inject.Inject

import me.dreamerzero.miniplaceholders.api.Expansion
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin
import me.dreamerzero.miniplaceholders.common.PluginConstants
import me.dreamerzero.miniplaceholders.connect.InternalPlatform

@Plugin(
    "miniplaceholders",
    "MiniPlaceholders",
    PluginConstants.VERSION,
    "Component based placeholders"
)
class KryptonPlugin @Inject constructor(
    val Server server,
    val Logger logger
) : PlaceholdersPlugin() {

    @Listener
    fun onStart(event: ServerStartEvent) {
        logger.info("Starting MiniPlaceholders Krypton")

        InternalPlatform.platform = InternalPlatform.KRYPTON

        loadDefaultExpansions()
        registerPlatformCommand()
    }

    override fun registerPlatFormCommand() {
        val command = PlaceholdersCommand<Sender>(
            { server.players.map { PlainTextComponentSerializer.plainText().serialize(it.name) } },
            { server.player(it) })
        val brigadierCMD = BrigadierCommand.of(command.placeholderTestCommand("miniplaceholders"))

        server.commandManager.register(brigadierCMD)
    }

    override fun loadDefaultExpansions() {
        Expansion.builder("server")
            .globalPlaceholder("total_entities", { queue, _ ->
                int entityCount = 0
                for (entry in server.worldManager.worlds.entrySet()) {
                    entityCount += entry.getValue().entities.size()
                }
                return TagsUtils.staticTag(Component.text(entityCount))
            })
            .globalPlaceholder("total_chunks", { queque, _ ->
                int chunkCount = 0
                for(entry in server.worldManager.worlds.entrySet()) {
                    chunkCount += entry.getValue().chunks.size()
                }
                return TagsUtils.staticTag(Component.text(chunkCount))
            })
            .globalPlaceholder("name", TagsUtils.staticTag(server.platform.name))
            
            .globalPlaceholder("online", { _, _ -> TagsUtils.staticTag(Component.text(server.players.size())) })
            .globalPlaceholder("max_players", TagsUtils.staticTag(Component.text(server.maxPlayers)))
            //.globalPlaceholder("has_whitelist", { _, _ -> TagsUtils.staticTag(Component.text(server.playerManager.whitelistEnabled)) })
        .build()
        .register()
        
    }
}