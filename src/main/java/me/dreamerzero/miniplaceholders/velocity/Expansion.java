package me.dreamerzero.miniplaceholders.velocity;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.miniplaceholders.velocity.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.GlobalPlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Expansion that contains placeholders
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
     * @param audience
     * @param otherAudience
     * @since 1.0.0
     * @return
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
     */
    public static interface Builder {
        /**
         * Adds an audience placeholder
         *
         * Example use:
         * <pre>
         *  Expansion.Builder builder = Expansion.builder("player");
         *  builder.audiencePlaceholder("name", audience -> {
         *      if(audience instanceof Player){
         *          return Component.text(((Player)audience).getUsername());
         *      } else {
         *          return Component.empty();
         *      }
         *  });
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
         * @param name the placeholder name
         * @param placeholder the placeholder
         * @since 1.0.0
         * @return the {@link Builder} itself
         */
        Builder audiencePlaceholder(@NotNull AudiencePlaceholder audiencePlaceholder);

        /**
         * Adds an Relational Placeholder based on two audiences
         *
         * This type of placeholder allows you to create
         * components based on a 2-audiences relationship,
         * one is the one on which the placeholder
         * is based and the other is the one on which
         * the placeholder is displayed.
         * @param name
         * @param placeholder
         * @since 1.0.0
         * @return the {@link Builder} itself
         */
        Builder relationalPlaceholder(@NotNull RelationalPlaceholder relationalPlaceholder);

        /**
         * Adds a global placeholder
         * @param name the placeholder name
         * @param tag the tag to be returned in case the name is matched
         * @return the {@link Builder} itself
         */
        Builder globalPlaceholder(@NotNull String name, @NotNull Tag tag);

        /**
         * Adds a global placeholder
         * @param globalPlaceholder the global placeholder
         * @return the {@link Builder} itself
         */
        Builder globalPlaceholder(@NotNull GlobalPlaceholder globalPlaceholder);

        /**
         * Adds a global component placeholder.
         * <p><bold>This placeholder is not cached</p>
         * @param name the placeholder name
         * @param component the component to return
         * @return the {@link Builder} itself
         */
        default Builder globalPlaceholder(@NotNull String name, @NotNull ComponentLike component) {
            return this.globalPlaceholder(name, Tag.selfClosingInserting(component));
        }

        /**
         * Build the Expansion
         * @return a new {@link Expansion}
         */
        @NotNull Expansion build();
    }
}
