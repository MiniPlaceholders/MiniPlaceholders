package me.dreamerzero.miniplaceholders.common;

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.api.enums.Platform;

public interface PlaceholdersPlugin {
    default void setPlatform(String string){
        MiniPlaceholders.setPlatform("velocity".equals(string) ? Platform.VELOCITY : Platform.PAPER);
    }
}
