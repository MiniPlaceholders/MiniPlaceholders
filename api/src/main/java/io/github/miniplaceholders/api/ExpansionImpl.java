package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import io.github.miniplaceholders.api.types.PlaceholderType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.*;
import java.util.function.BiFunction;

import static io.github.miniplaceholders.api.utils.Conditions.nonNullOrEmptyString;
import static java.util.Objects.requireNonNull;

// TODO: Support for ForwardingAudience
@NullMarked
final class ExpansionImpl implements Expansion {
  private static final AudiencePlaceholder<?>[] EMPTY_SINGLE_AUDIENCE = new AudiencePlaceholder[0];
  private static final RelationalPlaceholder<?>[] EMPTY_RELATIONAL_AUDIENCE = new RelationalPlaceholder[0];

  private final String name;
  private final AudiencePlaceholder<?>[] audiencePlaceholders;
  private final RelationalPlaceholder<?>[] relationalPlaceholders;
  private final TagResolver globalPlaceholders;
  @Nullable
  private final String author;
  @Nullable
  private final String version;

  ExpansionImpl(
          final String expansionName,
          @Nullable final Collection<AudiencePlaceholder<?>> audiencePlaceholders,
          @Nullable final Collection<RelationalPlaceholder<?>> relationalPlaceholders,
          @Nullable final TagResolver globalPlaceholders,
          @Nullable final String author,
          @Nullable final String version
  ) {
    this.name = expansionName;
    this.audiencePlaceholders = audiencePlaceholders != null
            ? audiencePlaceholders.toArray(EMPTY_SINGLE_AUDIENCE)
            : EMPTY_SINGLE_AUDIENCE;
    this.relationalPlaceholders = relationalPlaceholders != null
            ? relationalPlaceholders.toArray(EMPTY_RELATIONAL_AUDIENCE)
            : EMPTY_RELATIONAL_AUDIENCE;
    this.globalPlaceholders = globalPlaceholders == null
            ? TagResolver.empty()
            : globalPlaceholders;
    this.author = author;
    this.version = version;
  }

  @Override
  public @NotNull String name() {
    return this.name;
  }

  @Override
  public @Nullable String author() {
    return this.author;
  }

  @Override
  public @Nullable String version() {
    return this.version;
  }

  @Override
  public @NotNull TagResolver audiencePlaceholders() {
    if (audiencePlaceholders.length == 0) return TagResolver.empty();

    final TagResolver.Builder placeholders = TagResolver.builder();
    for (final AudiencePlaceholder<?> pl : this.audiencePlaceholders) {
      placeholders.resolver(pl);
    }
    return placeholders.build();
  }

  @Override
  public @NotNull TagResolver relationalPlaceholders() {
    if (relationalPlaceholders.length == 0) return TagResolver.empty();

    final TagResolver.Builder placeholders = TagResolver.builder();
    for (final RelationalPlaceholder<?> pl : this.relationalPlaceholders) {
      placeholders.resolver(pl);
    }
    return placeholders.build();
  }

  @Override
  public @NotNull TagResolver globalPlaceholders() {
    return this.globalPlaceholders;
  }

  @Override
  public void register() {
    if (this.registered()) {
      throw new IllegalStateException("Expansion" + this.name + "is already registered");
    }
    MiniPlaceholders.expansions.add(this);
  }

  @Override
  public void unregister() {
    if (!this.registered()) {
      throw new IllegalStateException("Expansion " + this.name + " is not registered");
    }
    MiniPlaceholders.expansions.remove(this);
  }

  @Override
  public boolean registered() {
    return MiniPlaceholders.expansions.contains(this);
  }

  @Override
  public Collection<AudiencePlaceholder<?>> registeredAudiencePlaceholders() {
    return List.of(audiencePlaceholders);
  }

  @Override
  public Collection<RelationalPlaceholder<?>> registeredRelationalPlaceholders() {
    return List.of(relationalPlaceholders);
  }

  @Override
  public @Nullable AudiencePlaceholder<?> audiencePlaceholderByName(String name) {
    requireNonNull(name);
    for (AudiencePlaceholder<?> audiencePlaceholder : audiencePlaceholders) {
      if (Objects.equals(audiencePlaceholder.key(), name)) {
        return audiencePlaceholder;
      }
    }
    return null;
  }

  @Override
  public @Nullable RelationalPlaceholder<?> relationalPlaceholderByName(String name) {
    requireNonNull(name);
    for (RelationalPlaceholder<?> relationalPlaceholder : relationalPlaceholders) {
      if (Objects.equals(relationalPlaceholder.key(), name)) {
        return relationalPlaceholder;
      }
    }
    return null;
  }

  @Override
  public TagResolver placeholdersByType(PlaceholderType type) {
    return switch (type) {
      case AUDIENCE -> audiencePlaceholders();
      case RELATIONAL -> relationalPlaceholders();
      case GLOBAL -> globalPlaceholders();
    };
  }

  @NullUnmarked
  static final class Builder implements Expansion.Builder {
    private final String expansionName;
    @Subst("server")
    private final String expansionPrefix;
    private Set<AudiencePlaceholder<?>> audiencePlaceholders;
    private Set<RelationalPlaceholder<?>> relationalPlaceholders;
    private TagResolver.Builder globalPlaceholders;
    private String author;
    private String version;

    Builder(@NotNull final String name) {
      this.expansionName = nonNullOrEmptyString(name, "Expansion name");
      this.expansionPrefix = name.toLowerCase(Locale.ROOT).concat("_");
    }

    @Override
    public <A extends Audience> @NotNull Builder audiencePlaceholder(
            final @Nullable Class<A> targetClass,
            final @NotNull String key,
            final @NotNull AudienceTagResolver<A> audiencePlaceholder
    ) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(audiencePlaceholder, "the audience placeholder cannot be null");

      if (this.audiencePlaceholders == null) this.audiencePlaceholders = new HashSet<>();

      this.audiencePlaceholders.add(AudiencePlaceholder.single(targetClass, expansionPrefix + key, audiencePlaceholder));
      return this;
    }

    @Override
    public <A extends Audience> @NotNull Builder relationalPlaceholder(
            @Nullable Class<A> targetClass,
            @NotNull String key,
            @NotNull RelationalTagResolver<A> relationalPlaceholder
    ) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(relationalPlaceholder, "the relational placeholder cannot be null");

      if (this.relationalPlaceholders == null) this.relationalPlaceholders = new HashSet<>();

      final var relationalTag = RelationalPlaceholder.relational(targetClass, expansionPrefix + "rel_" + key, relationalPlaceholder);
      this.relationalPlaceholders.add(relationalTag);
      return this;
    }

    @Override
    public @NotNull Builder globalPlaceholder(@Subst("time") @NotNull final String key, @NotNull final BiFunction<ArgumentQueue, Context, Tag> function) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(function, "the global placeholder cannot be null");

      if (this.globalPlaceholders == null) this.globalPlaceholders = TagResolver.builder();

      this.globalPlaceholders.tag(expansionPrefix + key, function);
      return this;
    }

    @Override
    public @NotNull Builder globalPlaceholder(@Subst("time") @NotNull final String key, @NotNull final Tag tag) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(tag, "the tag cannot be null");

      if (this.globalPlaceholders == null) this.globalPlaceholders = TagResolver.builder();

      this.globalPlaceholders.tag(expansionPrefix + key, tag);
      return this;
    }

    @Override
    public Expansion.@NotNull Builder author(@Nullable final String author) {
      this.author = author;
      return this;
    }

    @Override
    public Expansion.@NotNull Builder version(@Nullable final String version) {
      this.version = version;
      return this;
    }

    @Override
    public @NotNull Expansion build() {
      return new ExpansionImpl(
              this.expansionName,
              this.audiencePlaceholders,
              this.relationalPlaceholders,
              this.globalPlaceholders != null ? this.globalPlaceholders.build() : TagResolver.empty(),
              this.version,
              this.author
      );
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Expansion that)) return false;

    return that.name().equalsIgnoreCase(this.name());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name.hashCode());
  }
}
