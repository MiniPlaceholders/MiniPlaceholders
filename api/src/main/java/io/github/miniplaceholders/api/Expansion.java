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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
     * @return A TagResolver with variable placeholders between 2 {@link Audience}s
     * @since 1.0.0
     */
    TagResolver relationalPlaceholders();

    /**
     * Get global placeholders
     * @return the global placeholders
     * @since 1.0.0
     */
    TagResolver globalPlaceholders();

    /**
     * Register this expansion
     * @since 1.0.0
     * @throws IllegalStateException if the expansion you are trying to register is already registered
     * @see Expansion#registered()
     */
    void register();

    /**
     * Unregisters this expansion
     * @since 2.1.0
     * @throws IllegalStateException if the expansion you are trying to unregister is not registered
     * @see Expansion#registered()
     */
    void unregister();

    /**
     * Get the registration status of this expansion
     * @return true if the expansion is registered, otherwise false
     * @since 2.1.0
     */
    boolean registered();

    // TODO: Docs and Tests

    Collection<AudiencePlaceholder<?>> registeredAudiencePlaceholders();

    Collection<RelationalPlaceholder<?>> registeredRelationalPlaceholders();

    @Nullable AudiencePlaceholder<?> audiencePlaceholderByName(final String name);

    @Nullable RelationalPlaceholder<?> relationalPlaceholderByName(final String name);

    @Nullable GlobalPlaceholder globalPlaceholderByName(final String name);

    default boolean hasAudiencePlaceholder(final String name) {
        return audiencePlaceholderByName(name) != null;
    }

    default boolean hasRelationalPlaceholder(final String name) {
        return relationalPlaceholderByName(name) != null;
    }

    default boolean hasGlobalPlaceholder(final String name) {
        return globalPlaceholderByName(name) != null;
    }

    TagResolver placeholdersByType(PlaceholderType type);

    /**
     * Creates a new Expansion Builder
     * @param name the expansion name
     * @return a new expansion builder
     * @since 1.0.0
     */
    static Expansion.Builder builder(final String name){
        return new ExpansionImpl.Builder(name);
    }

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
     *
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
         * @param key the placeholder key, cannot be an empty or black string
         * @param audiencePlaceholder the single placeholder
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
         * @param key the placeholder key, cannot be an empty or black string
         * @param audiencePlaceholder the single placeholder
         * @return the {@link Builder} itself
         * @since 3.0.0
         */
        default Builder audiencePlaceholder(final String key, final AudienceTagResolver<Audience> audiencePlaceholder) {
            return this.audiencePlaceholder(null, key, audiencePlaceholder);
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
         * @param key the placeholder key, cannot be an empty or black string
         * @param relationalPlaceholder the relational placeholder
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
         * @param key the placeholder key, cannot be an empty or black string
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

        /**
         * Adds a global placeholder
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param key the placeholder key, cannot be an empty or black string
         * @param resolver the function
         * @return the {@link Builder} itself
         * @since 3.0.0
         */
        Builder globalPlaceholder(final String key, final GlobalTagResolver resolver);

        /**
         * Adds a global placeholder
         * 
         * <p>This placeholder is not cached and is immutable</p>
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
        @NotNull Builder author(final @Nullable String author);

        /**
         * Sets the version of this expansion
         *
         * @param version the version of this expansion
         * @return the {@link Builder} itself
         * @since 2.3.0
         */
        @Contract("_ -> this")
        @NotNull Builder version(final @Nullable String version);
    }
}
