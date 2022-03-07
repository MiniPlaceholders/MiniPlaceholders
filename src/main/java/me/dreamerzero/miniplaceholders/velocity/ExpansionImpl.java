package me.dreamerzero.miniplaceholders.velocity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.miniplaceholders.velocity.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.GlobalPlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.RelationalPlaceholder;
import me.dreamerzero.miniplaceholders.velocity.tag.PlaceholderTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ExpansionImpl implements Expansion {
    private final String name;
    private final Map<String, Function<Audience, Component>> audiencePlaceholders;
    private final Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders;
    private final TagResolver globalPlaceholders;

    ExpansionImpl(
        String expansionName,
        Map<String, Function<Audience, Component>> audiencePlaceholders,
        Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders,
        TagResolver globalPlaceholders){
            this.name = expansionName+"-";
            this.audiencePlaceholders = audiencePlaceholders;
            this.relationalPlaceholders = relationalPlaceholders;
            this.globalPlaceholders = globalPlaceholders;
    }

    @Override
    public String name(){
        return this.name;
    }

    @Override
    public TagResolver audiencePlaceholders(@NotNull Audience audience){
        if(audiencePlaceholders.isEmpty()) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");

        TagResolver.Builder placeholders = TagResolver.builder();
        audiencePlaceholders.forEach((st, func) ->
            placeholders.resolver(new PlaceholderTag(st){
                @Override
                public Tag tag(){
                    return Tag.selfClosingInserting(func.apply(audience));
                }
            })
        );
        return placeholders.build();
    }

    @Override
    public TagResolver relationalPlaceholders(@NotNull Audience audience, @NotNull Audience otherAudience){
        if(relationalPlaceholders.isEmpty()) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");
        Objects.requireNonNull(otherAudience, () -> "the other audience cannot be null");

        TagResolver.Builder placeholders = TagResolver.builder();
        relationalPlaceholders.forEach((st, bif) ->
            placeholders.resolver(new PlaceholderTag(st){
                @Override
                public Tag tag(){
                    return Tag.selfClosingInserting(bif.apply(audience, otherAudience));
                }
            })
        );
        return placeholders.build();
    }

    @Override
    public TagResolver globalPlaceholders(){
        return this.globalPlaceholders;
    }

    @Override
    public void register(){
        MiniPlaceholders.expansions.add(this);
    }

    static class Builder implements Expansion.Builder {
        private final String expansionName;
        private final Map<String, Function<Audience, Component>> audiencePlaceholders;
        private final Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders;
        private TagResolver.Builder globalPlaceholders;

        Builder(@NotNull String name){
            this.expansionName = name.toLowerCase(Locale.ROOT).concat("_");
            this.audiencePlaceholders = new HashMap<>();
            this.relationalPlaceholders = new HashMap<>();
            this.globalPlaceholders = TagResolver.builder();
        }

        @Override
        public Builder audiencePlaceholder(@NotNull AudiencePlaceholder audiencePlaceholder){
            Objects.requireNonNull(audiencePlaceholder, () -> "the audience placeholder cannot be null");

            audiencePlaceholders.put(expansionName+audiencePlaceholder.name(), audiencePlaceholder.get());
            return this;
        }

        @Override
        public Builder relationalPlaceholder(@NotNull RelationalPlaceholder relationalPlaceholder){
            Objects.requireNonNull(relationalPlaceholder, () -> "the relational placeholder cannot be null");

            relationalPlaceholders.put(expansionName+"rel_"+relationalPlaceholder.name(), relationalPlaceholder.get());
            return this;
        }

        @Override
        public Builder globalPlaceholder(@NotNull String name, @NotNull Tag placeholder){
            Objects.requireNonNull(name, () -> "the placeholder name cannot be null");
            Objects.requireNonNull(placeholder, () -> "the Tag cannot be null");

            globalPlaceholders.tag(expansionName+name, placeholder);
            return this;
        }

        @Override
        public Builder globalPlaceholder(@NotNull GlobalPlaceholder globalPlaceholder){
            Objects.requireNonNull(globalPlaceholder, ()-> "the global placeholder cannot be null");

            globalPlaceholders.resolver(new PlaceholderTag(expansionName+globalPlaceholder.name()) {
                @Override
                public Tag tag(){
                    return Tag.selfClosingInserting(globalPlaceholder.get().get());
                }
            });
            return this;
        }

        @Override
        public @NotNull Expansion build(){
            return new ExpansionImpl(expansionName, audiencePlaceholders, relationalPlaceholders, globalPlaceholders.build());
        }
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o == null || o.getClass() != ExpansionImpl.class) return false;
        return ((ExpansionImpl)o).name.equals(this.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name.hashCode());
    }
}
