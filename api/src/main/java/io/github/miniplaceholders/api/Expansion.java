package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import io.github.miniplaceholders.api.placeholder.RelationalPlaceholder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Expansion that contains placeholders
 *
 * <p>Usage Example:</p>
 * <pre>
 *  Player player = event.getPlayer();
 *  Expansion.Builder builder = Expansion.builder("player")
 *      .audiencePlaceholder("name", (p, queue, ctx) -> Tag.selfClosingInserting(Component.text(((Player)p).getUsername()))
 *      .build();
 *  Expansion expansion = builder.build();
 *  // You can also call the {@link Expansion#register} method to register
 *  // the {@link Expansion} in the {@link MiniPlaceholders} global Extensions and
 *  // use it in {@link MiniPlaceholders#getAudiencePlaceholders(Audience)} e.g.
 *  TagResolver resolver = expansion.audiencePlaceholder(player);
 *  player.sendMessage(MiniMessage.miniMessage().deserialize("Hello {@literal <luckperms_prefix> <player_name>}", resolver));
 * </pre>
 * 
 * @since 1.0.0
 */
public sealed interface Expansion permits ExpansionImpl {
    /**
     * Get the expansion name
     * @return the expansion name
     * @since 1.0.0
     */
    @NotNull String name();

    /**
     * Get the author of this expansion
     * @return the author or null
     * @since 2.2.5
     */
    @Nullable String author();

    /**
     * Get the version of this expansion
     * @return the version or null
     * @since 2.2.5
     */
    @Nullable String version();

    /**
     * Get the {@link TagResolver} of the desired {@link Audience}
     * @param audience the audience
     * @return A TagResolver with variable placeholders of an Audience
     * @since 1.0.0
     */
    @NotNull TagResolver audiencePlaceholders(final @NotNull Audience audience);

    /**
     * Get the relational placeholders based on two audiences
     *
     * <p>The resulting TagResolver will give results according
     * to the 2 {@link Audience} provided and when called
     * at any time will retrieve the required values</p>
     * @param audience the main audience
     * @param otherAudience the secondary audience
     * @return A TagResolver with variable placeholders between 2 {@link Audience}s
     * @since 1.0.0
     */
    @NotNull TagResolver relationalPlaceholders(final @NotNull Audience audience, final @NotNull Audience otherAudience);

    /**
     * Get global placeholders
     * @return the global placeholders
     * @since 1.0.0
     */
    @NotNull TagResolver globalPlaceholders();

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

    /**
     * Creates a new Expansion Builder
     * @param name the expansion name
     * @return a new expansion builder
     * @since 1.0.0
     */
    static @NotNull Expansion.Builder builder(final @NotNull String name){
        return new ExpansionImpl.Builder(name);
    }

    /**
     * Expansion Builder
     * <p>Example use:</p>
     * <pre>
     *  Expansion.Builder builder = Expansion.builder("player");
     *  builder
     *      // Thanks to this filter, a cast can be performed without the probability of a ClassCastException
     *      .filter(Player.class)
     *      .audiencePlaceholder("name", (audience, queue, ctx) -> Tag.selfClosingInserting(Component.text(((Player)audience).getUsername())));
     *  Expansion expansion = builder.build();
     * </pre>
     *
     * then use it:
     *
     * <pre>
     *  Player player = event.getPlayer();
     *  TagResolver resolver = expansion.audiencePlaceholder(player);
     *  Component messageReplaced = MiniMessage.deserialize({@link String}, resolver);
     * </pre>
     * 
     * @since 1.0.0
     */
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
        @NotNull Builder audiencePlaceholder(final @NotNull String key, final @NotNull AudiencePlaceholder audiencePlaceholder);

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
         * @since 1.0.0
         */
        @NotNull Builder relationalPlaceholder(final @NotNull String key, final @NotNull RelationalPlaceholder relationalPlaceholder);

        /**
         * Adds a global placeholder
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param key the placeholder key, cannot be an empty or black string
         * @param function the function
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @NotNull Builder globalPlaceholder(final @NotNull String key, final @NotNull BiFunction<@NotNull ArgumentQueue, @NotNull Context, @Nullable Tag> function);

        /**
         * Adds a global placeholder
         * 
         * <p>This placeholder is not cached and is immutable</p>
         * @param key the placeholder key, cannot be an empty or black string
         * @param tag the tag of this placeholder
         * @return the {@link Builder} itself
         * @since 1.1.0
         */
        @NotNull Builder globalPlaceholder(final @NotNull String key, final @NotNull Tag tag);

        /**
         * Filter the type of Audiences that this expansion can receive
         * <p>In case the {@link Expansion#audiencePlaceholders(Audience)} or
         * {@link Expansion#relationalPlaceholders(Audience, Audience)} method is called
         * and the provided audiences are not instances of
         * the specified class, a {@link TagResolver#empty} will be returned</p>
         *
         * <p>This eliminates the need to perform a manual
         * <pre>if(!(audience instanceof Player)) return TagResolver.empty();</pre>
         *
         * @param clazz the class to filter
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder filter(final @Nullable Class<? extends Audience> clazz);

        /**
         * Filters the audiences that this expansion can receive through a Predicate
         * <p>Example:</p>
         * <pre>
         *  Expansion.builder("example")
         *      .filter(aud -> aud instanceof Player player{@literal &&} isInProtectedServer(player)
         *      .audiencePlaceholder("hello", (aud, queue, ctx) -> Tag.selfInsertingClosing(Component.text("you are in protected server")))
         *      .build();
         * </pre>
         *
         * @param predicate the check to realize
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder filter(final @Nullable Predicate<@NotNull Audience> predicate);

        // TODO: javadocs

        /**
         * stuff here or something
         *
         * @param author the author of this expansion
         * @return the {@link Builder} itself
         * @since 2.2.5
         */
        @Contract("_ -> this")
        @NotNull Builder author(final @Nullable String author);

        // TODO: javadocs
        /**
         * stuff here or something
         *
         * @param version the version of this expansion
         * @return the {@link Builder} itself
         * @since 2.2.5
         */
        @Contract("_ -> this")
        @NotNull Builder version(final @Nullable String version);
    }
}
