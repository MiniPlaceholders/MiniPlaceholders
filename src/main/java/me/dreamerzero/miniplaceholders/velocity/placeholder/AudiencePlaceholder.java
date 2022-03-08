package me.dreamerzero.miniplaceholders.velocity.placeholder;

import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;

public class AudiencePlaceholder extends ExpansionPlaceholder<Function<Audience, Tag>> {
    private AudiencePlaceholder(String name, Function<Audience, Tag> function) {
        super(name, function);
    }

    public static AudiencePlaceholder create(@NotNull final String name, @NotNull final Function<Audience, Tag> function){
        return new AudiencePlaceholder(
            Objects.requireNonNull(name, () -> "the name cannot be null"),
            Objects.requireNonNull(function, () -> "the function cannot be null"));
    }
}
