package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.MiniPlaceholders;
import org.jspecify.annotations.NullMarked;

/**
 * Wrapper for platform-dependent instances.
 *
 * @param serverInstance The server instance.<br>
 *                       In Paper it will be {@code org.bukkit.Server},<br>
 *                       in Velocity it will be {@code com.velocitypowered.api.proxy.ProxyServer},<br>
 *                       in Sponge it will be {@code org.spongepowered.api.Server},<br>
 *                       and in Fabric it will be {@code net.minecraft.server.MinecraftServer}.
 * @param complementInstance The main class of MiniPlaceholders, either as a plugin or mod.<br>
 *                       In Paper it will be {@code org.bukkit.plugin.java.JavaPlugin},<br>
 *                       in Velocity it will be {@code io.github.miniplaceholders.velocity.VelocityPlugin},<br>
 *                       in Sponge it will be {@code io.github.miniplaceholders.sponge.SpongePlugin},<br>
 *                       and in Fabric it will be {@code net.fabricmc.api.ModInitializer}.
 * @see MiniPlaceholders#platform()
 */
@NullMarked
public record PlatformData(Object serverInstance, Object complementInstance) {
}
