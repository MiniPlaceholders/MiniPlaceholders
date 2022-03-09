package me.dreamerzero.miniplaceholders.api;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class Tags {
    private Tags(){}

    public static TagResolver global(String placeholderName, Tag tag){
        return TagResolver.resolver(new TagResolver.WithoutArguments() {
            @Override
            public @Nullable Tag resolve(@NotNull String name) {
                return placeholderName.equalsIgnoreCase(name) ? tag : null;
            }
        });
    }

    static Relational relational(String name, BiFunction<Audience, Audience, Tag> function){
        return new Tags.Relational(name, function);
    }

    static Single single(String name, Function<Audience, Tag> function){
        return new Single(name, function);
    }

    static final class Relational {
        private final BiFunction<Audience, Audience, Tag> function;
        private final String placeholderName;

        Relational(String name, BiFunction<Audience, Audience, Tag> function){
            this.placeholderName = name;
            this.function = function;
        }

        TagResolver of(Audience audience, Audience otherAudience){
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

    static final class Single {
        private final Function<Audience, Tag> function;
        private final String placeholderName;

        private Single(String name, Function<Audience, Tag> function){
            this.placeholderName = name;
            this.function = function;
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
}
