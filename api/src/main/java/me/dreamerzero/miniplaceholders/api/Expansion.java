package me.dreamerzero.miniplaceholders.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.GlobalPlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Expansion that contains placeholders
 *
 * <p>Usage Example:</p>
 * <pre>
 *  Player player = event.getPlayer();
 *  Expansion.Builder builder = Expansion.builder("player")
 *      .audiencePlaceholder(AudiencePlaceholder.create("name", p -> Tag.selfClosingInserting(Component.text(((Player)p).getUsername())))
 *      .build();
 *  Expansion expansion = builder.build();
 *  // You can also call the {@link Expansion#register} method to register
 *  // the {@link Extension} in the {@link MiniPlaceholders} global Extensions and
 *  // use it in {@link MiniPlaceholders#getAudiencePlaceholders(Audience)} e.g.
 *  TagResolver resolver = expansion.audiencePlaceholder(player);
 *  player.sendMessage(MiniMessage.miniMessage().deserialize("Hello <luckperms_prefix> <player_name>", resolver));
 * </pre>
 */
public interface Expansion {
    /**
     * Get the expansion name
     * @return the expansion name
     */
    @NotNull String name();

    /**
     * Get the {@link TagResolver} of the desired {@link Audience}
     * @param audience
     * @since 1.0.0
     * @return
     */
    @NotNull TagResolver audiencePlaceholders(@NotNull Audience audience);

    /**
     * Get the relational placeholders based on two audiences
     *
     * <p>The resulting TagResolver will give results according
     * to the 2 {@link Audience} provided and when called
     * at any time will retrieve the required values</p>
     * @param audience the main audience
     * @param otherAudience the secondary audience
     * @since 1.0.0
     * @return A TagResolver with variable placeholders between 2 Audiences
     */
    @NotNull TagResolver relationalPlaceholders(@NotNull Audience audience, @NotNull Audience otherAudience);

    /**
     * Get global placeholders
     * @since 1.0.0
     * @return the global placeholders
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
     */
    public static Expansion.Builder builder(@NotNull String name){
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
     *      .audiencePlaceholder(AudiencePlaceholder.create("name", audience -> Tag.selfClosingInserting(Component.text(((Player)audience).getUsername()))));
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
     */
    public static interface Builder {
        /**
         * Adds an audience placeholder
         *
         * <p>This type of Placeholder depends on a specific audience to obtain its values</p>
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param audiencePlaceholder the single placeholder
         * @since 1.0.0
         * @return the {@link Builder} itself
         */
        Builder audiencePlaceholder(@NotNull AudiencePlaceholder audiencePlaceholder);

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
         * @param relationalPlaceholder the relation placeholder
         * @since 1.0.0
         * @return the {@link Builder} itself
         */
        Builder relationalPlaceholder(@NotNull RelationalPlaceholder relationalPlaceholder);

        /**
         * Adds a global placeholder
         *
         * <p>The content of this Placeholder is cached
         * and can mutate depending on when it is invoked</p>
         * @param globalPlaceholder the global placeholder
         * @return the {@link Builder} itself
         */
        Builder globalPlaceholder(@NotNull GlobalPlaceholder globalPlaceholder);

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
         */
        Builder filter(@Nullable Class<? extends Audience> clazz);

        /**
         * Filters the audiences that this expansion can receive through a Predicate
         * <p>Example:</p>
         * <pre>
         *  Expansion.builder("example").filter(Player.class).filter(aud -> isInProtectedServer((Player)aud))
         *      .audiencePlaceholder(AudiencePlaceholder.create("hello", aud -> Tag.selfInsertingClosing(Component.text("you are in protected server"))))
         *      .build();
         * </pre>
         * @param predicate the check to realize
         * @return the {@link Builder} itself
         */
        Builder filter(@NotNull Predicate<Audience> predicate);

        /**
         * Build the Expansion
         * @return a new {@link Expansion}
         */
        @NotNull Expansion build();
    }
}
