package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.placeholder.*;
import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import io.github.miniplaceholders.api.types.PlaceholderType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.*;

import static io.github.miniplaceholders.api.utils.Conditions.nonNullOrEmptyString;
import static java.util.Objects.requireNonNull;

@NullMarked
final class ExpansionImpl implements Expansion {
  private static final AudiencePlaceholder<?>[] EMPTY_SINGLE_AUDIENCE = new AudiencePlaceholder[0];
  private static final RelationalPlaceholder<?>[] EMPTY_RELATIONAL_AUDIENCE = new RelationalPlaceholder[0];
  private static final GlobalPlaceholder[] EMPTY_GLOBAL_PLACEHOLDER = new GlobalPlaceholder[0];

  private final String name;
  private final PlaceholderTagResolver audiencePlaceholders;
  private final PlaceholderTagResolver relationalPlaceholders;
  private final PlaceholderTagResolver globalPlaceholders;
  @Nullable
  private final String author;
  @Nullable
  private final String version;

  ExpansionImpl(
          final String expansionName,
          @Nullable final Collection<AudiencePlaceholder<?>> audiencePlaceholders,
          @Nullable final Collection<RelationalPlaceholder<?>> relationalPlaceholders,
          @Nullable final Collection<GlobalPlaceholder> globalPlaceholders,
          @Nullable final String author,
          @Nullable final String version
  ) {
    this.name = expansionName;
    this.audiencePlaceholders = audiencePlaceholders != null
            ? new PlaceholderTagResolver(audiencePlaceholders.toArray(EMPTY_SINGLE_AUDIENCE))
            : PlaceholderTagResolver.EMPTY;
    this.relationalPlaceholders = relationalPlaceholders != null
            ? new PlaceholderTagResolver(relationalPlaceholders.toArray(EMPTY_RELATIONAL_AUDIENCE))
            : PlaceholderTagResolver.EMPTY;
    this.globalPlaceholders = globalPlaceholders == null
            ? PlaceholderTagResolver.EMPTY
            : new PlaceholderTagResolver(globalPlaceholders.toArray(EMPTY_GLOBAL_PLACEHOLDER));
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
    if (audiencePlaceholders.placeholders().length == 0) return TagResolver.empty();

    return this.audiencePlaceholders;
  }

  @Override
  public @NotNull TagResolver relationalPlaceholders() {
    if (relationalPlaceholders.placeholders().length == 0) return TagResolver.empty();

    return this.relationalPlaceholders;
  }

  @Override
  public @NotNull TagResolver globalPlaceholders() {
    if (globalPlaceholders.placeholders().length == 0) return TagResolver.empty();

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
    return List.of((AudiencePlaceholder<?>[]) audiencePlaceholders.placeholders());
  }

  @Override
  public Collection<RelationalPlaceholder<?>> registeredRelationalPlaceholders() {
    return List.of((RelationalPlaceholder<?>[]) relationalPlaceholders.placeholders());
  }

  @Override
  public @Nullable AudiencePlaceholder<?> audiencePlaceholderByName(String name) {
    requireNonNull(name);
    for (Placeholder audiencePlaceholder : audiencePlaceholders.placeholders()) {
      if (Objects.equals(audiencePlaceholder.key(), name)) {
        return (AudiencePlaceholder<?>) audiencePlaceholder;
      }
    }
    return null;
  }

  @Override
  public @Nullable RelationalPlaceholder<?> relationalPlaceholderByName(String name) {
    requireNonNull(name);
    for (Placeholder relationalPlaceholder : relationalPlaceholders.placeholders()) {
      if (Objects.equals(relationalPlaceholder.key(), name)) {
        return (RelationalPlaceholder<?>) relationalPlaceholder;
      }
    }
    return null;
  }

  @Override
  public @Nullable GlobalPlaceholder globalPlaceholderByName(String name) {
    for (Placeholder placeholder : globalPlaceholders.placeholders()) {
      if (Objects.equals(placeholder.key(), name)) {
        return (GlobalPlaceholder) placeholder;
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
    private Set<GlobalPlaceholder> globalPlaceholders;
    private String author;
    private String version;

    Builder(@NotNull final String name) {
      this.expansionName = nonNullOrEmptyString(name, "Expansion name");
      this.expansionPrefix = name.toLowerCase(Locale.ROOT).concat("_");
    }

    @Override
    public <A extends Audience> @NotNull Builder audiencePlaceholder(
            final @Nullable Class<A> targetClass,
            @Subst("name") final @NotNull String key,
            final @NotNull AudienceTagResolver<@NotNull A> audiencePlaceholder
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
            @Subst("relation") @NotNull String key,
            @NotNull RelationalTagResolver<@NotNull A> relationalPlaceholder
    ) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(relationalPlaceholder, "the relational placeholder cannot be null");

      if (this.relationalPlaceholders == null) this.relationalPlaceholders = new HashSet<>();

      final var relationalTag = RelationalPlaceholder.relational(targetClass, expansionPrefix + "rel_" + key, relationalPlaceholder);
      this.relationalPlaceholders.add(relationalTag);
      return this;
    }

    @Override
    public @NotNull Builder globalPlaceholder(
            @Subst("time") @NotNull final String key,
            @NotNull final GlobalTagResolver function
    ) {
      nonNullOrEmptyString(key, "Placeholder key");
      requireNonNull(function, "the global placeholder cannot be null");

      if (this.globalPlaceholders == null) this.globalPlaceholders = new HashSet<>();

      final var globalResolver = new GlobalPlaceholder(expansionPrefix + key, function);
      this.globalPlaceholders.add(globalResolver);
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
              this.globalPlaceholders,
              this.author,
              this.version
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

  @Override
  public String toString() {
    return "ExpansionImpl{" +
            "name='" + name + '\'' +
            ", audiencePlaceholders=" + audiencePlaceholders +
            ", relationalPlaceholders=" + relationalPlaceholders +
            ", globalPlaceholders=" + globalPlaceholders +
            ", author='" + author + '\'' +
            ", version='" + version + '\'' +
            '}';
  }
}
