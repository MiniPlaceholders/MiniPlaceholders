package io.github.miniplaceholders.api;

import java.util.Objects;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import io.github.miniplaceholders.api.placeholder.RelationalPlaceholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class Tags {
    private Tags(){}

    static Relational relational(@NotNull final String name, @NotNull final RelationalPlaceholder relationalPlaceholder){
        return new Tags.Relational(name, relationalPlaceholder);
    }

    static Single single(@NotNull final String name, @NotNull final AudiencePlaceholder audiencePlaceholder){
        return new Single(name, audiencePlaceholder);
    }

    static final class Relational {
        private final RelationalPlaceholder relationalPlaceholder;
        private final String key;

        Relational(final String key, final RelationalPlaceholder relationalPlaceholder){
            this.relationalPlaceholder = relationalPlaceholder;
            this.key = key;
        }

        TagResolver of(@NotNull final Audience audience, @NotNull final Audience otherAudience){
            return new TagResolver(){
                @Override
                public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments,
                        @NotNull Context ctx) throws ParsingException {

                    return this.has(name)
                        ? relationalPlaceholder.tag(audience, otherAudience, arguments, ctx)
                        : null;
                }

                @Override
                public boolean has(@NotNull String name) {
                    return key.equalsIgnoreCase(name);
                }
            };
        }

        @Override
        public boolean equals(Object o){
            if(o == this) return true;
            if(!(o instanceof Relational)) return false;
            return ((Relational)o).key.equals(this.key);
        }

        @Override
        public int hashCode(){
            return Objects.hash(key);
        }
    }

    static final class Single {
        private final AudiencePlaceholder audiencePlaceholder;
        private final String key;

        private Single(@NotNull final String key, @NotNull final AudiencePlaceholder audiencePlaceholder){
            this.key = key;
            this.audiencePlaceholder = audiencePlaceholder;
        }

        TagResolver of(@NotNull final Audience audience){
            return new TagResolver() {

                @Override
                public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments,
                        @NotNull Context ctx) throws ParsingException {
                    return this.has(name)
                        ? audiencePlaceholder.tag(audience, arguments, ctx)
                        : null;
                }

                @Override
                public boolean has(@NotNull String name) {
                    return key.equalsIgnoreCase(name);
                }
            };
        }

        @Override
        public boolean equals(Object o){
            if(o == this) return true;
            if(!(o instanceof Single)) return false;
            return ((Single)o).key.equals(this.key);
        }

        @Override
        public int hashCode(){
            return Objects.hash(key);
        }
    }
}
