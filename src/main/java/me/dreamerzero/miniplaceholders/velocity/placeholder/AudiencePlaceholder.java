package me.dreamerzero.miniplaceholders.velocity.placeholder;

import java.util.function.Function;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class AudiencePlaceholder extends ExpansionPlaceholder<Function<Audience, Component>> {
    private AudiencePlaceholder(String name, Function<Audience, Component> function) {
        super(name, function);
    }

    public static AudiencePlaceholder create(String name, Function<Audience, Component> function){
        return new AudiencePlaceholder(name, function);
    }
}
