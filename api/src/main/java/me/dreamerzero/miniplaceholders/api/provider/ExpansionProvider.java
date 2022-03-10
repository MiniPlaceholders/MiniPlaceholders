package me.dreamerzero.miniplaceholders.api.provider;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.miniplaceholders.api.Expansion;

public interface ExpansionProvider {
    boolean canRegister();

    Expansion getExpansion();

    default @NotNull Logger getLogger() {
        return Logger.getLogger(this.getClass().getName());
    }
}
