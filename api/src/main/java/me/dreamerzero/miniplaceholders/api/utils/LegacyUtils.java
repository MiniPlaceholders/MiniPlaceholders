package me.dreamerzero.miniplaceholders.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class LegacyUtils {
    private LegacyUtils(){}

    public static @NotNull Component parsePossibleLegacy(@Nullable String string) {
        if(string == null || string.isBlank()) return Component.empty();
        if(string.indexOf('&') != 0){
            return LegacyComponentSerializer.builder()
                .character('&')
                .hexColors()
                .build()
                .deserialize(string);
        } else {
            return MiniMessage.miniMessage()
                .deserialize(string);
        }
    }
}
