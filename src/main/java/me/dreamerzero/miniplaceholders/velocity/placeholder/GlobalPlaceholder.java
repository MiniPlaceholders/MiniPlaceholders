package me.dreamerzero.miniplaceholders.velocity.placeholder;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.minimessage.tag.Tag;

public class GlobalPlaceholder extends ExpansionPlaceholder<Tag> {
    private GlobalPlaceholder(String name, Tag tag){
        super(name, tag);
    }
    public static GlobalPlaceholder create(@NotNull final String name, @NotNull final Tag tag){
        return new GlobalPlaceholder(
            Objects.requireNonNull(name, () -> "the placeholder name cannot be null"),
            Objects.requireNonNull(tag, () -> "the tag cannot be null"));
    }
}
