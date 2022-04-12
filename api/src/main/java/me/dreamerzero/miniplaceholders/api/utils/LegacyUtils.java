package me.dreamerzero.miniplaceholders.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

/**
 * Legacy utils
 */
public final class LegacyUtils {
    private LegacyUtils(){}

    /**
     * Legacy serializer with hex support
     * 
     * Use this as the last available alternative,
     * it is recommended to use MiniMessage instead of this serializer
     */
    public static final LegacyComponentSerializer LEGACY_HEX_SERIALIZER = LegacyComponentSerializer.builder()
        .character('&')
        .hexColors()
        .build();

    /**
     * Parse a string with possible legacy symbols
     * @param string the string
     * @return a parsed string
     * @since 1.0.0
     */
    public static @NotNull Component parsePossibleLegacy(@Nullable String string) {
        if(string == null || string.isBlank()) return Component.empty();
        if(string.indexOf('&') != 0){
            return miniMessage().deserialize(
                miniMessage().serialize(LEGACY_HEX_SERIALIZER.deserialize(string))
            );
        } else {
            return miniMessage().deserialize(string);
        }
    }
}
