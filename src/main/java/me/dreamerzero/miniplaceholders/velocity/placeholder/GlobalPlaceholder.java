package me.dreamerzero.miniplaceholders.velocity.placeholder;

import java.util.function.Supplier;

import net.kyori.adventure.text.Component;

public class GlobalPlaceholder extends ExpansionPlaceholder<Supplier<Component>> {
    private GlobalPlaceholder(String name, Supplier<Component> supplier){
        super(name, supplier);
    }
    public static GlobalPlaceholder create(String name, Supplier<Component> supplier){
        return new GlobalPlaceholder(name, supplier);
    }
}
