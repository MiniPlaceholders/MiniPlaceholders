package me.dreamerzero.miniplaceholders.velocity.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Internal
public final class GlobalTag {
    private GlobalTag(){}
    public static TagResolver create(String placeholderName, Tag tag){
        return TagResolver.resolver(new TagResolver.WithoutArguments() {
            @Override
            public @Nullable Tag resolve(@NotNull String name) {
                return placeholderName.equalsIgnoreCase(name) ? tag : null;
            }
        });
    }
}
