package me.dreamerzero.miniplaceholders.velocity.tag;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Internal
public final class SingleTag {
    private final Function<Audience, Tag> function;
    private final String placeholderName;

    private SingleTag(String name, Function<Audience, Tag> function){
        this.placeholderName = name;
        this.function = function;
    }

    public static SingleTag create(String name, Function<Audience, Tag> function){
        return new SingleTag(name, function);
    }

    public TagResolver of(Audience audience){
        return TagResolver.resolver(new TagResolver.WithoutArguments() {
            @Override
            public @Nullable Tag resolve(@NotNull String name) {
                if(name.equalsIgnoreCase(placeholderName)){
                    return function.apply(audience);
                } else {
                    return null;
                }
            }
        });
    }
}
