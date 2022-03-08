package me.dreamerzero.miniplaceholders.velocity.placeholder;

import org.jetbrains.annotations.NotNull;

abstract class ExpansionPlaceholder<F> {
    private String name;
    private F function;
    protected ExpansionPlaceholder(String name, F function){
        this.name = name;
        this.function = function;
    }
    public @NotNull String name() {
        return this.name;
    }

    public @NotNull F get() {
        return this.function;
    }
}
