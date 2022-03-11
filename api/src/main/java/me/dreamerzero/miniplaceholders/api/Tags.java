package me.dreamerzero.miniplaceholders.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class Tags {
    private Tags(){}

    static Relational relational(String name, RelationalPlaceholder relationalPlaceholder){
        return new Tags.Relational(name, relationalPlaceholder);
    }

    static Single single(String name, AudiencePlaceholder audiencePlaceholder){
        return new Single(name, audiencePlaceholder);
    }

    static final class Relational {
        private final RelationalPlaceholder relationalPlaceholder;
        private final String key;

        Relational(String key, RelationalPlaceholder relationalPlaceholder){
            this.relationalPlaceholder = relationalPlaceholder;
            this.key = key;
        }

        TagResolver of(Audience audience, Audience otherAudience){
            return TagResolver.resolver(new TagResolver(){
                @Override
                public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments,
                        @NotNull Context ctx) throws ParsingException {
                    if(this.has(name)){
                        return relationalPlaceholder.tag(audience, otherAudience, arguments, ctx);
                    }
                    return null;
                }

                @Override
                public boolean has(@NotNull String name) {
                    return key.equalsIgnoreCase(name);
                }
            });
        }
    }

    static final class Single {
        private final AudiencePlaceholder audiencePlaceholder;
        private final String key;

        private Single(String key, AudiencePlaceholder audiencePlaceholder){
            this.key = key;
            this.audiencePlaceholder = audiencePlaceholder;
        }

        public TagResolver of(Audience audience){
            return TagResolver.resolver(new TagResolver() {

                @Override
                public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments,
                        @NotNull Context ctx) throws ParsingException {
                    return this.has(name) ? audiencePlaceholder.tag(audience, arguments, ctx) : null;
                }

                @Override
                public boolean has(@NotNull String name) {
                    return key.equalsIgnoreCase(name);
                }
            });
        }
    }
}
