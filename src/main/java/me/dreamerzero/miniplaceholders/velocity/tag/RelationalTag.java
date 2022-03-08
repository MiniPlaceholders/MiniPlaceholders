package me.dreamerzero.miniplaceholders.velocity.tag;

import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Internal
public final class RelationalTag {
    private final BiFunction<Audience, Audience, Tag> function;
    private final String placeholderName;

    private RelationalTag(String name, BiFunction<Audience, Audience, Tag> function){
        this.placeholderName = name;
        this.function = function;
    }

    public static RelationalTag create(String name, BiFunction<Audience, Audience, Tag> function){
        return new RelationalTag(name, function);
    }

    public TagResolver of(Audience audience, Audience otherAudience){
        return TagResolver.resolver(new TagResolver.WithoutArguments(){
            @Override
            public @Nullable Tag resolve(@NotNull String name) {
                if(name.equalsIgnoreCase(placeholderName)){
                    return function.apply(audience, otherAudience);
                } else {
                    return null;
                }
            }
        });
    }
}
