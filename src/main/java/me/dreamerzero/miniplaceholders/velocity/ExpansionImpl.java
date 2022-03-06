package me.dreamerzero.miniplaceholders.velocity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    public TagResolver audiencePlaceholders(Audience audience){
        TagResolver.Builder placeholders = TagResolver.builder();
        audiencePlaceholders.forEach((st, func) ->
            placeholders.resolver(Placeholder.component(st, func.apply(audience)))
        );
        return placeholders.build();
    }

    @Override
    public TagResolver relationalPlaceholders(Audience audience, Audience otherAudience){
        TagResolver.Builder placeholders = TagResolver.builder();
        relationalPlaceholders.forEach((st, bif) ->
            placeholders.resolver(Placeholder.component(st, bif.apply(audience, otherAudience)))
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

        Builder(String name){
            this.expansionName = name.toLowerCase(Locale.ROOT).concat("_");
            this.audiencePlaceholders = new HashMap<>();
            this.relationalPlaceholders = new HashMap<>();
            this.globalPlaceholders = TagResolver.builder();
        }

        @Override
        public Builder audiencePlaceholder(String name, Function<Audience, Component> placeholder){
            audiencePlaceholders.put(expansionName+name, placeholder);
            return this;
        }

        @Override
        public Builder relationalPlaceholder(String name, BiFunction<Audience, Audience, Component> placeholder){
            relationalPlaceholders.put(expansionName+"rel_"+name, placeholder);
            return this;
        }

        @Override
        public Builder globalPlaceholder(String name, Tag placeholder){
            globalPlaceholders.tag(expansionName+name, placeholder);
            return this;
        }

        @Override
        public Builder globalPlaceholder(String name, TagResolver resolver){
            globalPlaceholders.resolver(resolver);
            return this;
        }

        @Override
        public Expansion build(){
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
