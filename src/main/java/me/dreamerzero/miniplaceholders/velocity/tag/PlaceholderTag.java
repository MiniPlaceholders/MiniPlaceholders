package me.dreamerzero.miniplaceholders.velocity.tag;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public abstract class PlaceholderTag implements TagResolver.WithoutArguments {
    private String name;
    protected PlaceholderTag(String fullName){
        this.name = fullName;
    }

    abstract Tag tag();

    @Override
    public @Nullable Tag resolve(@NotNull String name) {
        return name.equalsIgnoreCase(this.name) ? tag() : null;
    }

    public static class Relational {
        private final BiFunction<Audience, Audience, Component> function;
        private final String name;

        protected Relational(String name, BiFunction<Audience, net.kyori.adventure.audience.Audience, Component> function){
            this.name = name;
            this.function = function;
        }

        public static Relational create(String name, BiFunction<Audience, Audience, Component> function){
            return new Relational(name, function);
        }

        public TagResolver of(Audience audience, Audience otherAudience){
            return TagResolver.resolver(new PlaceholderTag(name) {
                @Override
                public Tag tag() {
                    return Tag.selfClosingInserting(function.apply(audience, otherAudience));
                }
            });
        }
    }

    public static class Single {
        private final Function<Audience, Component> function;
        private final String name;

        protected Single(String name, Function<Audience, Component> function){
            this.name = name;
            this.function = function;
        }

        public static Single create(String name, Function<Audience, Component> function){
            return new Single(name, function);
        }

        public TagResolver of(Audience audience){
            return TagResolver.resolver(new PlaceholderTag(name) {
                @Override
                public Tag tag() {
                    return Tag.selfClosingInserting(function.apply(audience));
                }
            });
        }
    }

    public static class Global {
        private Global(){}
        public static TagResolver create(String name, Supplier<Component> function){
            return TagResolver.resolver(new PlaceholderTag(name) {
                @Override
                public Tag tag() {
                    return Tag.selfClosingInserting(function.get());
                }
            });
        }
    }
}
