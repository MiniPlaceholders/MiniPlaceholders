package me.dreamerzero.miniplaceholders.api;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.GlobalPlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
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
        @NotNull Set<Tags.Single> audiencePlaceholders,
        @NotNull Set<Tags.Relational> relationalPlaceholders,
        @NotNull TagResolver globalPlaceholders,
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
        if(audiencePlaceholders.isEmpty()) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");

        if(this.singleFilter(audience)) return TagResolver.empty();

        TagResolver.Builder placeholders = TagResolver.builder();
        audiencePlaceholders.forEach(pl -> placeholders.resolver(pl.of(audience)));
        return placeholders.build();
    }

    private boolean singleFilter(Audience audience){
        return filterClass != null && !filterClass.isInstance(audience)
            || predicateFilter != null && predicateFilter.test(audience);
    }

    @Override
    public @NotNull TagResolver relationalPlaceholders(@NotNull Audience audience, @NotNull Audience otherAudience){
        if(relationalPlaceholders.isEmpty()) return TagResolver.empty();

        Objects.requireNonNull(audience, () -> "the audience cannot be null");
        Objects.requireNonNull(otherAudience, () -> "the other audience cannot be null");

        if(this.relationalFilter(audience, otherAudience)) return TagResolver.empty();

        TagResolver.Builder placeholders = TagResolver.builder();
        relationalPlaceholders.forEach(pl -> placeholders.resolver(pl.of(audience, otherAudience)));
        return placeholders.build();
    }

    private boolean relationalFilter(Audience audience, Audience otherAudience){
        return (filterClass != null && (!filterClass.isInstance(audience) || !filterClass.isInstance(otherAudience)))
            || (predicateFilter != null && (!predicateFilter.test(audience) || !predicateFilter.test(otherAudience)));
    }

    @Override
    public @NotNull TagResolver globalPlaceholders(){
        return this.globalPlaceholders;
    }

    @Override
    public void register(){
        MiniPlaceholders.expansions.add(this);
    }

    static class Builder implements Expansion.Builder {
        private final String expansionName;
        private final Set<Tags.Single> audiencePlaceholders;
        private final Set<Tags.Relational> relationalPlaceholders;
        private final TagResolver.Builder globalPlaceholders;
        private Class<? extends Audience> filterClass;
        private Predicate<Audience> predicateFilter;

        Builder(@NotNull String name){
            this.expansionName = name.toLowerCase(Locale.ROOT).concat("_");
            this.audiencePlaceholders = new HashSet<>();
            this.relationalPlaceholders = new HashSet<>();
            this.globalPlaceholders = TagResolver.builder();
        }

        @Override
        public Builder audiencePlaceholder(@NotNull AudiencePlaceholder audiencePlaceholder){
            Objects.requireNonNull(audiencePlaceholder, () -> "the audience placeholder cannot be null");

            audiencePlaceholders.add(Tags.single(expansionName+audiencePlaceholder.name(), audiencePlaceholder.get()));
            return this;
        }

        @Override
        public Builder relationalPlaceholder(@NotNull RelationalPlaceholder relationalPlaceholder){
            Objects.requireNonNull(relationalPlaceholder, () -> "the relational placeholder cannot be null");

            relationalPlaceholders.add(Tags.relational(expansionName+"rel_"+relationalPlaceholder.name(), relationalPlaceholder.get()));
            return this;
        }

        @Override
        public Builder globalPlaceholder(@NotNull GlobalPlaceholder globalPlaceholder){
            Objects.requireNonNull(globalPlaceholder, () -> "the global placeholder cannot be null");

            globalPlaceholders.resolver(Tags.global(expansionName+globalPlaceholder.name(), globalPlaceholder.get()));
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
                expansionName,
                audiencePlaceholders,
                relationalPlaceholders,
                globalPlaceholders.build(),
                filterClass,
                predicateFilter
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
