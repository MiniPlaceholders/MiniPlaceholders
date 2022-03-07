package me.dreamerzero.miniplaceholders.velocity.tag;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        private final BiFunction<net.kyori.adventure.audience.Audience, net.kyori.adventure.audience.Audience, Component> function;
        private final String name;

        protected Relational(String name, BiFunction<net.kyori.adventure.audience.Audience, net.kyori.adventure.audience.Audience, Component> function){
            this.name = name;
            this.function = function;
        }

        public static Relational create(String name, BiFunction<net.kyori.adventure.audience.Audience, net.kyori.adventure.audience.Audience, Component> function){
            return new Relational(name, function);
        }

        public TagResolver of(net.kyori.adventure.audience.Audience audience, net.kyori.adventure.audience.Audience otherAudience){
            return TagResolver.resolver(new PlaceholderTag(name) {
                @Override
                public Tag tag() {
                    return Tag.selfClosingInserting(function.apply(audience, otherAudience));
                }
            });
        }
    }

    public static class Audience {
        private final Function<net.kyori.adventure.audience.Audience, Component> function;
        private final String name;

        protected Audience(String name, Function<net.kyori.adventure.audience.Audience, Component> function){
            this.name = name;
            this.function = function;
        }

        public static Audience create(String name, Function<net.kyori.adventure.audience.Audience, Component> function){
            return new Audience(name, function);
        }

        public TagResolver of(net.kyori.adventure.audience.Audience audience){
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
