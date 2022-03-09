package me.dreamerzero.miniplaceholders.testobjects;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.audience.Audience;

public final class TestAudience implements Audience {
    private final String name;
    public TestAudience(String name) {
        this.name = name;
    }

    public @NotNull String getName(){
        return this.name;
    }
}
