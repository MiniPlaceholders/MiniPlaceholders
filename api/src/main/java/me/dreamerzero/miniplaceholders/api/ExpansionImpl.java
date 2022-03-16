package me.dreamerzero.miniplaceholders.api;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ExpansionImpl implements Expansion {
    private final String name;
    private final Set<Tags.Single> audiencePlaceholders;
    private final Set<Tags.Relational> relationalPlaceholders;
    private final TagResolver globalPlaceholders;
    private final Class<? extends Audience> filterClass;
    private final Predicate<Audience> predicateFilter;

    ExpansionImpl(
        @NotNull String expansionName,
        @Nullable Set<Tags.Single> audiencePlaceholders,
        @Nullable Set<Tags.Relational> relationalPlaceholders,
        @Nullable TagResolver globalPlaceholders,
        @Nullable Class<? extends Audience> filterClass,
        @Nullable Predicate<Audience> predicateFilter){
            this.name = expansionName+"-";
            this.audiencePlaceholders = audiencePlaceholders;
            this.relationalPlaceholders = relationalPlaceholders;
            this.globalPlaceholders = globalPlaceholders;
            this.filterClass = filterClass;
            this.predicateFilter = predicateFilter;
    }

    @Override
    public String name(){
        return this.name;
    }

    @Override
    public @NotNull TagResolver audiencePlaceholders(@NotNull Audience audience){
        if(audiencePlaceholders == null) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");

        if(this.singleFilter(audience)) return TagResolver.empty();

        TagResolver.Builder placeholders = TagResolver.builder();
        for(Tags.Single pl : this.audiencePlaceholders){
            placeholders.resolver(pl.of(audience));
        }
        return placeholders.build();
    }

    private boolean singleFilter(Audience audience){
        return filterClass != null && !filterClass.isInstance(audience)
            || predicateFilter != null && predicateFilter.test(audience);
    }

    @Override
    public @NotNull TagResolver relationalPlaceholders(@NotNull Audience audience, @NotNull Audience otherAudience){
        if(relationalPlaceholders == null) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");
        Objects.requireNonNull(otherAudience, () -> "the other audience cannot be null");

        if(this.relationalFilter(audience, otherAudience)) return TagResolver.empty();

        TagResolver.Builder placeholders = TagResolver.builder();
        for(Tags.Relational pl : this.relationalPlaceholders){
            placeholders.resolver(pl.of(audience, otherAudience));
        }
        return placeholders.build();
    }

    private boolean relationalFilter(Audience audience, Audience otherAudience){
        return filterClass != null && (!filterClass.isInstance(audience) || !filterClass.isInstance(otherAudience))
            || predicateFilter != null && (!predicateFilter.test(audience) || !predicateFilter.test(otherAudience));
    }

    @Override
    public @NotNull TagResolver globalPlaceholders(){
        return this.globalPlaceholders != null ? this.globalPlaceholders : TagResolver.empty();
    }

    @Override
    public void register(){
        MiniPlaceholders.expansions.add(this);
    }

    static class Builder implements Expansion.Builder {
        private final String expansionName;
        private Set<Tags.Single> audiencePlaceholders;
        private Set<Tags.Relational> relationalPlaceholders;
        private TagResolver.Builder globalPlaceholders;
        private Class<? extends Audience> filterClass;
        private Predicate<Audience> predicateFilter;

        Builder(@NotNull String name){
            this.expansionName = Objects.requireNonNull(name, () -> "the placeholder name cannot be null")
                .toLowerCase(Locale.ROOT).concat("_");
        }

        @Override
        public Builder audiencePlaceholder(@NotNull String key, @NotNull AudiencePlaceholder audiencePlaceholder){
            Objects.requireNonNull(key, () -> "the placeholder key cannot be null");
            Objects.requireNonNull(audiencePlaceholder, () -> "the audience placeholder cannot be null");

            if(this.audiencePlaceholders == null) this.audiencePlaceholders = new HashSet<>(5);

            this.audiencePlaceholders.add(Tags.single(expansionName+key, audiencePlaceholder));
            return this;
        }

        @Override
        public Builder relationalPlaceholder(@NotNull String key, @NotNull RelationalPlaceholder relationalPlaceholder){
            Objects.requireNonNull(key, () -> "the placeholder key cannot be null");
            Objects.requireNonNull(relationalPlaceholder, () -> "the relational placeholder cannot be null");

            if(this.relationalPlaceholders == null) this.relationalPlaceholders = new HashSet<>(4);

            this.relationalPlaceholders.add(Tags.relational(expansionName+"rel_"+key, relationalPlaceholder));
            return this;
        }

        @Override
        public Builder globalPlaceholder(@NotNull String key,
                BiFunction<ArgumentQueue, Context, Tag> function){
            Objects.requireNonNull(key, () -> "the placeholder key cannot be null");
            Objects.requireNonNull(function, () -> "the global placeholder cannot be null");

            if(this.globalPlaceholders == null) this.globalPlaceholders = TagResolver.builder();

            this.globalPlaceholders.tag(expansionName+key, function);
            return this;
        }

        @Override
        public Builder filter(Class<? extends Audience> clazz) {
            this.filterClass = clazz;
            return this;
        }

        @Override
        public Builder filter(Predicate<Audience> predicate){
            this.predicateFilter = predicate;
            return this;
        }

        @Override
        public @NotNull Expansion build(){
            return new ExpansionImpl(
                this.expansionName,
                this.audiencePlaceholders,
                this.relationalPlaceholders,
                this.globalPlaceholders != null ? this.globalPlaceholders.build() : null,
                this.filterClass,
                this.predicateFilter
            );
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
