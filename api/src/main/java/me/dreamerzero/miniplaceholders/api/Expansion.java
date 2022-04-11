package me.dreamerzero.miniplaceholders.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
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
public interface Expansion {
    /**
     * Get the expansion name
     * @return the expansion name
     * @since 1.0.0
     */
    @NotNull String name();

    /**
     * Get the {@link TagResolver} of the desired {@link Audience}
     * @param audience the audience
     * @return A TagResolver with variable placeholders of an Audience
     * @since 1.0.0
     */
    @NotNull TagResolver audiencePlaceholders(@NotNull final Audience audience);

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
    @NotNull TagResolver relationalPlaceholders(@NotNull final Audience audience, @NotNull final Audience otherAudience);

    /**
     * Get global placeholders
     * @return the global placeholders
     * @since 1.0.0
     */
    @NotNull TagResolver globalPlaceholders();

    /**
     * Register this expansion
     * @since 1.0.0
     */
    void register();

    /**
     * Creates a new Expansion Builder
     * @param name the expansion name
     * @return a new expansion builder
     * @since 1.0.0
     */
    public static @NotNull Expansion.Builder builder(@NotNull final String name){
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
    public static interface Builder extends AbstractBuilder<Expansion> {
        /**
         * Adds an audience placeholder
         *
         * <p>This type of Placeholder depends on a specific audience to obtain its values</p>
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param key the placeholder key, cannot be an empty or black string
         * @param audiencePlaceholder the single placeholder
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @NotNull Builder audiencePlaceholder(@NotNull final String key, @NotNull final AudiencePlaceholder audiencePlaceholder);

        /**
         * Adds an Relational Placeholder based on two audiences
         *
         * <p>This type of placeholder allows you to create
         * components based on a 2-audiences relationship,
         * one is the one on which the placeholder
         * is based and the other is the one on which
         * the placeholder is displayed.</p>
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param key the placeholder key, cannot be an empty or black string
         * @param relationalPlaceholder the relational placeholder
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @NotNull Builder relationalPlaceholder(@NotNull final String key, @NotNull final RelationalPlaceholder relationalPlaceholder);

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
        @NotNull Builder globalPlaceholder(@NotNull final String key, final BiFunction<ArgumentQueue, Context, Tag> function);

        /**
         * Filter the type of Audiences that this expansion can receive
         * <p>In case the {@link Expansion#audiencePlaceholders(Audience)} or
         * {@link Expansion#relationalPlaceholders(Audience, Audience)} method is called
         * and the provided audiences are not instances of
         * the specified class, a {@link TagResolver#empty} will be returned</p>
         *
         * <p>This eliminates the need to perform a manual
         * <pre>if(!(audience instanceof Player)) return TagResolver.empty();</pre>
         * @param clazz the class to filter
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @NotNull Builder filter(@Nullable final Class<? extends Audience> clazz);

        /**
         * Filters the audiences that this expansion can receive through a Predicate
         * <p>Example:</p>
         * <pre>
         *  Expansion.builder("example").filter(Player.class).filter(aud -> isInProtectedServer((Player)aud))
         *      .audiencePlaceholder("hello", (aud, queue, ctx) -> Tag.selfInsertingClosing(Component.text("you are in protected server")))
         *      .build();
         * </pre>
         * @param predicate the check to realize
         * @return the {@link Builder} itself
         * @since 1.0.0
         */
        @NotNull Builder filter(@NotNull final Predicate<Audience> predicate);
    }
}
