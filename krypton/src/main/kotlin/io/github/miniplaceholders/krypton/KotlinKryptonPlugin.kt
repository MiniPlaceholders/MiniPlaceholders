package io.github.miniplaceholders.krypton

import com.google.inject.Inject
import io.github.miniplaceholders.api.utils.TagsUtils
import io.github.miniplaceholders.common.PlaceholdersCommand
import io.github.miniplaceholders.common.PlaceholdersPlugin
import io.github.miniplaceholders.common.PluginConstants
import io.github.miniplaceholders.connect.InternalPlatform
import io.github.miniplaceholders.kotlin.asClosingTag
import io.github.miniplaceholders.kotlin.expansion
import me.lucko.spark.api.statistic.StatisticWindow
import net.kyori.adventure.key.Key
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
                Component.text(entityCount).asClosingTag()
            }
            globalPlaceholder("total_chunks") { _, _ ->
                var chunkCount = 0
                for ((_, value) in server.worldManager.worlds) {
                    chunkCount += value.chunks.size
                }
                Component.text(chunkCount).asClosingTag()
            }
            globalPlaceholder("name", TagsUtils.staticTag(server.platform.name))
            globalPlaceholder("online") { queue, _ ->
                if (queue.hasNext()) {
                    val worldName = queue.pop().value()
                    val players = server.worldManager[Key.key(worldName)]?.players?.size
                    if (players != null) {
                        return@globalPlaceholder Component.text(players).asClosingTag()
                    }

                }
                Component.text(server.players.size).asClosingTag()
            }
            globalPlaceholder("version", TagsUtils.staticTag(server.platform.version))
            globalPlaceholder("max_players", Component.text(server.maxPlayers).asClosingTag())
            globalPlaceholder("has_whitelist") { _, _ -> Component.text(server.playerManager.whitelistEnabled).asClosingTag() }
            globalPlaceholder("tps_1") { _, _ -> Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_1)).asClosingTag() }
            globalPlaceholder("tps_5") { _, _ -> Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_5)).asClosingTag() }
            globalPlaceholder("tps_15") { _, _ -> Component.text(server.spark.tps()!!.poll(StatisticWindow.TicksPerSecond.MINUTES_15)).asClosingTag() }
            globalPlaceholder("mspt") { _, _ -> Component.text(server.spark.mspt()!!.poll(StatisticWindow.MillisPerTick.SECONDS_10).mean()).asClosingTag() }
        }.register()
    }

    override fun registerPlatformCommand() {
        val command = PlaceholdersCommand<Sender>(
            {
                server.players
                    .map { it.name }
                    .map { plainText().serialize(it) }
            }, server::player
        )
        val brigadierCMD = BrigadierCommand.of(command.placeholderTestCommand("miniplaceholders"))
        server.commandManager.register(brigadierCMD)
    }
}