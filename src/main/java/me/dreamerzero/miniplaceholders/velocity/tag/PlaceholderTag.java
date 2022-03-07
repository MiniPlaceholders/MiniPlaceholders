package me.dreamerzero.miniplaceholders.velocity.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public abstract class PlaceholderTag implements TagResolver.WithoutArguments {
    private String name;
    protected PlaceholderTag(String fullName){
        this.name = fullName;
    }

    public abstract Tag tag();

    @Override
    public @Nullable Tag resolve(@NotNull String name) {
        return name.equalsIgnoreCase(this.name) ? tag() : null;
    }
}
