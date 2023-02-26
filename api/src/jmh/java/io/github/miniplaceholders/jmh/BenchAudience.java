package io.github.miniplaceholders.jmh;

import java.util.UUID;

import net.kyori.adventure.audience.Audience;

public class BenchAudience implements Audience {
    private final String name;
    private final UUID uuid;
    public BenchAudience(String name){
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public String getName(){
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }
}
