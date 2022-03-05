package me.dreamerzero.miniplaceholders.velocity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Expansion that contains placeholders
 */
final class ExpansionImpl implements Expansion {
    private final String name;
    private final Map<String, Function<Audience, Component>> audiencePlaceholders;
    private final Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders;
    private final Map<String, Supplier<Component>> globalPlaceholders;
    private final Consumer<String> debugConsumer;

    ExpansionImpl(
        String expansionName,
        Map<String, Function<Audience, Component>> audiencePlaceholders,
        Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders,
        Map<String, Supplier<Component>> globalPlaceholders,
        Consumer<String> debugConsumer){
            this.name = expansionName+"-";
            this.audiencePlaceholders = audiencePlaceholders;
            this.relationalPlaceholders = relationalPlaceholders;
            this.globalPlaceholders = globalPlaceholders;
            if(debugConsumer != null) this.debugConsumer = debugConsumer;
            else this.debugConsumer = st -> {};
    }

    @Override
    public String getExpansionName(){
        return this.name;
    }

    @Override
    public TagResolver getAudiencePlaceholders(Audience audience){
        TagResolver.Builder placeholders = TagResolver.builder();
        audiencePlaceholders.forEach((st, func) -> {
            placeholders.resolver(Placeholder.component(st, func.apply(audience)));
            debugConsumer.accept("Added Placeholder" +st+"to Expansion"+name);
        });
        return placeholders.build();
    }

    @Override
    public TagResolver getRelationalPlaceholders(Audience audience, Audience otherAudience){
        TagResolver.Builder placeholders = TagResolver.builder();
        relationalPlaceholders.forEach((st, bif) ->
            placeholders.resolver(Placeholder.component(st, bif.apply(audience, otherAudience)))
        );
        return placeholders.build();
    }

    @Override
    public TagResolver getGlobalPlaceholders(){
        TagResolver.Builder placeholders = TagResolver.builder();
        globalPlaceholders.forEach((st, func) ->
            placeholders.resolver(Placeholder.component(st, func.get()))
        );
        return placeholders.build();
    }

    static class Builder implements Expansion.Builder {
        private final String expansionName;
        private final Map<String, Function<Audience, Component>> audiencePlaceholders;
        private final Map<String, BiFunction<Audience, Audience, Component>> relationalPlaceholders;
        private final Map<String, Supplier<Component>> globalPlaceholders;

        Builder(String name){
            this.expansionName = name.toLowerCase(Locale.ROOT).concat("-");
            this.audiencePlaceholders = new HashMap<>();
            this.relationalPlaceholders = new HashMap<>();
            this.globalPlaceholders = new HashMap<>();
        }

        @Override
        public Builder addAudiencePlaceholder(String name, Function<Audience, Component> placeholder){
            audiencePlaceholders.put(expansionName+name, placeholder);
            return this;
        }

        @Override
        public Builder addRelationalPlaceholder(String name, BiFunction<Audience, Audience, Component> placeholder){
            relationalPlaceholders.put(expansionName+"rel-"+name, placeholder);
            return this;
        }

        @Override
        public Builder addGlobalPlaceholder(String name, Supplier<Component> placeholder){
            globalPlaceholders.put(expansionName+name, placeholder);
            return this;
        }

        @Override
        public Expansion build(){
            ExpansionImpl exp = new ExpansionImpl(expansionName, audiencePlaceholders, relationalPlaceholders, globalPlaceholders, null);
            MiniPlaceholders.expansions.add(exp);
            return exp;
        }
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof ExpansionImpl)) return false;
        return ((ExpansionImpl)o).name.equals(this.name);
    }

    @Override
    public int hashCode(){
        return 31 + name.hashCode();
    }
}
