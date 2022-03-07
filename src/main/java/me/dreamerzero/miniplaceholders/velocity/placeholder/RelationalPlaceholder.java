package me.dreamerzero.miniplaceholders.velocity.placeholder;

import java.util.function.BiFunction;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class RelationalPlaceholder extends ExpansionPlaceholder<BiFunction<Audience, Audience, Component>> {
    private RelationalPlaceholder(String name, BiFunction<Audience, Audience, Component> function) {
        super(name, function);
    }

    public static RelationalPlaceholder create(String name, BiFunction<Audience, Audience, Component> function){
        return new RelationalPlaceholder(name, function);
    }
}
