package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import io.github.miniplaceholders.api.placeholder.GlobalPlaceholder;
import io.github.miniplaceholders.api.placeholder.RelationalPlaceholder;
import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import io.github.miniplaceholders.api.types.PlaceholderType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

/**
 * Expansion that contains placeholders
 *
 * <p>Usage Example:</p>
 * <pre>
 *  Player player = event.getPlayer();
 *  Expansion.Builder builder = Expansion.builder("player")
 *      .resolver(Player.class, "name", (p, queue, ctx) -> Tag.selfClosingInserting(Component.text(p.getUsername()))
 *      .build();
 *  Expansion expansion = builder.build();
 *  // You can also call the {@link Expansion#register} method to register
 *  // the {@link Expansion} in the {@link MiniPlaceholders} global Extensions and
 *  // use it in {@link MiniPlaceholders#audiencePlaceholders()} e.g.
 *  TagResolver resolver = expansion.resolver();
 *  player.sendMessage(MiniMessage.miniMessage().deserialize("Hello {@literal <luckperms_prefix> <player_name>}", player, resolver));
 * </pre>
 *
 * @since 1.0.0
 */
@NullMarked
public sealed interface Expansion permits ExpansionImpl {
  /**
   * Get the expansion name
   *
   * @return the expansion name
   * @since 1.0.0
   */
  String name();

  /**
   * Get the author of this expansion
   *
   * @return the author or null
   * @since 2.3.0
   */
  @Nullable String author();

  /**
   * Get the version of this expansion
   *
   * @return the version or null
   * @since 2.3.0
   */
  @Nullable String version();

  /**
   * Get the {@link TagResolver} of an {@link Audience}
   *
   * @return A TagResolver with variable placeholders of an Audience
   * @since 1.0.0
   */
  TagResolver audiencePlaceholders();

  /**
   * Get the relational placeholders based on two audiences
   *
   * <p>The resulting TagResolver will give results according
   * to the 2 {@link Audience} provided and when called
   * at any time will retrieve the required values</p>
   *
   * @return A TagResolver with variable placeholders between 2 {@link Audience}s
   * @since 1.0.0
   */
  TagResolver relationalPlaceholders();

  /**
   * Get global placeholders
   *
   * @return the global placeholders
   * @since 1.0.0
   */
  TagResolver globalPlaceholders();

  /**
   * Register this expansion
   *
   * @throws IllegalStateException if the expansion you are trying to register is already registered
   * @see Expansion#registered()
   * @since 1.0.0
   */
  void register();

  /**
   * Unregisters this expansion
   *
   * @throws IllegalStateException if the expansion you are trying to unregister is not registered
   * @see Expansion#registered()
   * @since 2.1.0
   */
  void unregister();

  /**
   * Get the registration status of this expansion
   *
   * @return true if the expansion is registered, otherwise false
   * @since 2.1.0
   */
  boolean registered();

  /**
   * Get the registered audience placeholders
   *
   * @return registered audience placeholders
   */
  Collection<AudiencePlaceholder<?>> registeredAudiencePlaceholders();

  /**
   * Get the registered relational placeholders
   *
   * @return registered relational placeholders
   */
  Collection<RelationalPlaceholder<?>> registeredRelationalPlaceholders();

  /**
   * Get an Audience Placeholder by name
   *
   * <pre>{@code
   *      Expansion expansion = MiniPlaceholders.expansionByName("player")
   *      AudiencePlaceholder<?> placeholder = expansion.audiencePlaceholderByName("name");
   * }</pre>
   *
   * @param name the name of the placeholder to search
   * @return the desired placeholder or null
   */
  @Nullable AudiencePlaceholder<?> audiencePlaceholderByName(final String name);

  /**
   * Get a Relational Placeholder by name
   *
   * <pre>{@code
   *      Expansion expansion = MiniPlaceholders.expansionByName("expansion")
   *      RelationalPlaceholder<?> placeholder = expansion.relationalPlaceholderByName("placeholder");
   * }</pre>
   *
   * @param name the name of the placeholder to search
   * @return the desired placeholder or null
   * @apiNote The name of a relational placeholder does not contain the prefix “rel”,
   * just the name assigned when it was created
   */
  @Nullable RelationalPlaceholder<?> relationalPlaceholderByName(final String name);

  /**
   * Get a Global Placeholder by name
   *
   * <pre>{@code
   *      Expansion expansion = MiniPlaceholders.expansionByName("player")
   *      GlobalPlaceholder placeholder = expansion.globalPlaceholderByName("name");
   * }</pre>
   *
   * @param name the name of the placeholder to search
   * @return the desired placeholder or null
   */
  @Nullable GlobalPlaceholder globalPlaceholderByName(final String name);

  /**
   * Checks whether an audience placeholder with the specified name has been registered in this expansion.
   *
   * @param name the name of the placeholder to check
   * @return true if the placeholder is registered, else false
   */
  default boolean hasAudiencePlaceholder(final String name) {
    return audiencePlaceholderByName(name) != null;
  }

  /**
   * Checks whether a relational placeholder with the specified name has been registered in this expansion.
   *
   * @param name the name of the placeholder to check
   * @return true if the placeholder is registered, else false
   */
  default boolean hasRelationalPlaceholder(final String name) {
    return relationalPlaceholderByName(name) != null;
  }

  /**
   * Checks whether a global placeholder with the specified name has been registered in this expansion.
   *
   * @param name the name of the placeholder to check
   * @return true if the placeholder is registered, else false
   */
  default boolean hasGlobalPlaceholder(final String name) {
    return globalPlaceholderByName(name) != null;
  }

  /**
   * Get a TagResolver that contains all placeholders of a specific type in this expansion.
   *
   * @param type the desired type
   * @return a TagResolver containing all placeholder of a specific type
   */
  TagResolver placeholdersByType(PlaceholderType type);

  /**
   * Creates a new Expansion Builder
   *
   * @param name the expansion name
   * @return a new expansion builder
   * @since 1.0.0
   */
  static Expansion.Builder builder(final String name) {
    return new ExpansionImpl.Builder(name);
  }

  /**
   * Get a shorter ToString with the details of this expansion.
   *
   * @return a shorter ToString
   */
  @ApiStatus.Internal
  @Deprecated(forRemoval = true, since = "3.1.0")
  default String shortToString() {
    final StringBuilder builder = new StringBuilder(name()).append('[');
    if (author() != null) {
      builder.append("Author: ").append(author());
    }
    if (version() != null) {
      if (author() != null) {
        builder.append(", ");
      }
      builder.append("Version: ").append(version());
    }
    builder.append(']');
    return builder.toString();
  }

  /**
   * Expansion Builder
   * <p>Example use:</p>
   * <pre>
   *  Expansion.Builder builder = Expansion.builder("player");
   *  builder
   *      // Thanks to this filter, a cast can be performed without the probability of a ClassCastException
   *      .filter(Player.class)
   *      .resolver("name", (audience, queue, ctx) -> Tag.selfClosingInserting(Component.text(((Player)audience).getUsername())));
   *  Expansion expansion = builder.build();
   * </pre>
   * <p>
   * then use it:
   *
   * <pre>
   *  Player player = event.getPlayer();
   *  TagResolver resolver = expansion.resolver(player);
   *  Component messageReplaced = MiniMessage.deserialize({@link String}, resolver);
   * </pre>
   *
   * @since 1.0.0
   */
  @NullMarked
  interface Builder extends AbstractBuilder<Expansion> {

    /**
     * Adds an audience placeholder
     *
     * <p>This type of Placeholder depends on a specific audience to obtain its values</p>
     *
     * <p>The content of this Placeholder is cached
     * and can mutate depending on when it is invoked</p>
     *
     * @param targetClass         the target class
     * @param key                 the placeholder key, cannot be an empty or black string
     * @param audiencePlaceholder the single placeholder
     * @param <A>                 the type of audience that this placeholder supports, must be the same as targetClass
     * @return the {@link Builder} itself
     * @since 1.0.0
     */
    <A extends Audience> Builder audiencePlaceholder(
        final @Nullable Class<A> targetClass,
        final String key,
        final AudienceTagResolver<A> audiencePlaceholder
    );

    /**
     * Adds an audience placeholder
     *
     * <p>This type of Placeholder depends on a specific audience to obtain its values</p>
     *
     * <p>The content of this Placeholder is cached
     * and can mutate depending on when it is invoked</p>
     *
     * @param key                 the placeholder key, cannot be an empty or black string
     * @param audiencePlaceholder the single placeholder
     * @return the {@link Builder} itself
     * @since 3.0.0
     */
    default Builder audiencePlaceholder(final String key, final AudienceTagResolver<Audience> audiencePlaceholder) {
      return this.audiencePlaceholder(null, key, audiencePlaceholder);
    }

    <A extends Audience> Builder audiencePlaceholder(
        final Class<A> targetClass,
        final AudiencePlaceholder.Builder.Provider<A> builderProvider
    );

    default Builder audiencePlaceholder(final AudiencePlaceholder.Builder.Provider<Audience> builderProvider) {
      return audiencePlaceholder(Audience.class, builderProvider);
    }

    /**
     * Adds a Relational Placeholder based on two audiences
     *
     * <p>This type of placeholder allows you to create
     * components based on a 2-audiences relationship,
     * one is the one on which the placeholder
     * is based and the other is the one on which
     * the placeholder is displayed.</p>
     *
     * <p>The content of this Placeholder is cached
     * and can mutate depending on when it is invoked</p>
     *
     * @param targetClass           the target class
     * @param key                   the placeholder key, cannot be an empty or black string
     * @param relationalPlaceholder the relational placeholder
     * @param <A>                   the type of audience that this placeholder supports, must be the same as targetClass
     * @return the {@link Builder} itself
     * @since 3.0.0
     */
    <A extends Audience> Builder relationalPlaceholder(
        final @Nullable Class<A> targetClass,
        final String key,
        final RelationalTagResolver<A> relationalPlaceholder
    );

    /**
     * Adds a Relational Placeholder based on two audiences
     *
     * <p>This type of placeholder allows you to create
     * components based on a 2-audiences relationship,
     * one is the one on which the placeholder
     * is based and the other is the one on which
     * the placeholder is displayed.</p>
     *
     * <p>The content of this Placeholder is cached
     * and can mutate depending on when it is invoked</p>
     *
     * @param key                the placeholder key, cannot be an empty or black string
     * @param relationalResolver the relational placeholder
     * @return the {@link Builder} itself
     * @since 3.0.0
     */
    default Builder relationalPlaceholder(
        final String key,
        final RelationalTagResolver<Audience> relationalResolver
    ) {
      return this.relationalPlaceholder(null, key, relationalResolver);
    }

    <A extends Audience> Builder relationalPlaceholder(
        final Class<A> targetClass,
        final RelationalPlaceholder.Builder.Provider<A> builderProvider
    );

    default Builder relationalPlaceholder(final RelationalPlaceholder.Builder.Provider<Audience> builderProvider) {
      return relationalPlaceholder(Audience.class, builderProvider);
    }

    /**
     * Adds a global placeholder
     *
     * <p>The content of this Placeholder is cached
     * and can mutate depending on when it is invoked</p>
     *
     * @param key      the placeholder key, cannot be an empty or black string
     * @param resolver the function
     * @return the {@link Builder} itself
     * @since 3.0.0
     */
    Builder globalPlaceholder(final String key, final GlobalTagResolver resolver);

    /**
     * Adds a global placeholder
     *
     * <p>This placeholder is not cached and is immutable</p>
     *
     * @param key the placeholder key, cannot be an empty or black string
     * @param tag the tag of this placeholder
     * @return the {@link Builder} itself
     * @since 1.1.0
     */
    default Builder globalPlaceholder(final String key, final Tag tag) {
      return this.globalPlaceholder(key, (queue, context) -> tag);
    }

    /**
     * Sets the author of this expansion
     *
     * @param author the author of this expansion
     * @return the {@link Builder} itself
     * @since 2.3.0
     */
    @Contract("_ -> this")
    Builder author(final @Nullable String author);

    /**
     * Sets the version of this expansion
     *
     * @param version the version of this expansion
     * @return the {@link Builder} itself
     * @since 2.3.0
     */
    @Contract("_ -> this")
    Builder version(final @Nullable String version);
  }
}
