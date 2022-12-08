package me.dreamerzero.miniplaceholders.krypton

import com.google.inject.Inject
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils
import me.dreamerzero.miniplaceholders.common.PlaceholdersCommand
import me.dreamerzero.miniplaceholders.common.PlaceholdersPlugin
import me.dreamerzero.miniplaceholders.common.PluginConstants
import me.dreamerzero.miniplaceholders.connect.InternalPlatform
import me.dreamerzero.miniplaceholders.kotlin.expansion
import me.lucko.spark.api.statistic.StatisticWindow
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.plugin.annotation.Plugin
import org.kryptonmc.krypton.KryptonServer

@Plugin(
    name = "MiniPlaceholders",
    id = "miniplaceholders",
    version = PluginConstants.VERSION,
    authors = ["4drian3d"]
)
class KotlinKryptonPlugin @Inject constructor(
    private val server: KryptonServer,
    private val logger: Logger
): PlaceholdersPlugin {

    init {
        InternalPlatform.platform(InternalPlatform.KRYPTON)
    }

    @Listener
    @Suppress("UNUSED")
    fun onServerStart(event: ServerStartEvent) {
        logger.info("Starting MiniPlaceholders Krypton")
        this.loadDefaultExpansions()
        this.registerPlatformCommand()
    }

    override fun loadDefaultExpansions() {
        expansion("server") {
            globalPlaceholder("total_entities") { _, _ ->
                var entityCount = 0
                for ((_, value) in server.worldManager.worlds) {
                    entityCount += value.entities.size
                }
                TagsUtils.staticTag(Component.text(entityCount))
            }
            globalPlaceholder("total_chunks") { _, _ ->
                var chunkCount = 0
                for ((_, value) in server.worldManager.worlds) {
                    chunkCount += value.chunks.size
                }
                TagsUtils.staticTag(Component.text(chunkCount))
            }
            globalPlaceholder("name", TagsUtils.staticTag(server.platform.name))
            globalPlaceholder("online") { _, _ -> TagsUtils.staticTag(Component.text(server.players.size)) }
            globalPlaceholder("version", TagsUtils.staticTag(server.platform.version))
            globalPlaceholder("max_players", TagsUtils.staticTag(Component.text(server.maxPlayers)))
            globalPlaceholder("has_whitelist") { _, _ -> TagsUtils.staticTag(Component.text(server.playerManager.whitelistEnabled)) }
            globalPlaceholder("tps_1") { _, _ -> TagsUtils.staticTag(Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_1))) }
            globalPlaceholder("tps_5") { _, _ -> TagsUtils.staticTag(Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_5))) }
            globalPlaceholder("tps_15") { _, _ -> TagsUtils.staticTag(Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_15))) }
            globalPlaceholder("mspt") { _, _ -> TagsUtils.staticTag(Component.text(server.spark.mspt()!!.poll(StatisticWindow.MillisPerTick.SECONDS_10).mean())) }
        }.register()
    }

    override fun registerPlatformCommand() {
        val command = PlaceholdersCommand<Sender>(
            { server.players
                    .map { it.name }
                    .map { plainText().serialize(it) }
            }, (server::player))
        val brigadierCMD = BrigadierCommand.of(command.placeholderTestCommand("miniplaceholders"))
        server.commandManager.register(brigadierCMD)
    }
}