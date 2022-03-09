package me.dreamerzero.miniplaceholders.api.placeholder;

import java.util.Objects;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;

public class RelationalPlaceholder extends ExpansionPlaceholder<BiFunction<Audience, Audience, Tag>> {
    private RelationalPlaceholder(String name, BiFunction<Audience, Audience, Tag> function) {
        super(name, function);
    }

    public static RelationalPlaceholder create(@NotNull final String name, @NotNull final BiFunction<Audience, Audience, Tag> function){
        return new RelationalPlaceholder(
            Objects.requireNonNull(name, () -> "the name cannot be null"),
            Objects.requireNonNull(function, () -> "the function cannot be null"));
    }
}
