package me.dreamerzero.miniplaceholders.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Legacy utils
 */
public final class LegacyUtils {
    private LegacyUtils(){}

    /**
     * Parse a string with possible legacy symbols
     * @param string the string
     * @return a parsed string
     * @since 1.0.0
     */
    public static @NotNull Component parsePossibleLegacy(@Nullable String string) {
        if(string == null || string.isBlank()) return Component.empty();
        if(string.indexOf('&') != 0){
            return MiniMessage.miniMessage().deserialize(
                MiniMessage.miniMessage()
                .serialize(LegacyComponentSerializer.builder()
                    .character('&')
                    .hexColors()
                    .build()
                    .deserialize(string)
                )
            );
        } else {
            return MiniMessage.miniMessage()
                .deserialize(string);
        }
    }
}
