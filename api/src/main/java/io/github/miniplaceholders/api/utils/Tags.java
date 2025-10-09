package io.github.miniplaceholders.api.utils;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jspecify.annotations.Nullable;

/**
 * Tags basic instances.
 *
 * @since 3.0.0
 */
public final class Tags {
    private Tags() {
    }

    /**
     * Empty Tag
     *
     * @since 3.0.0
     */
    public static final Tag EMPTY_TAG = Tag.selfClosingInserting(Component.empty());

    /**
     * Null Tag
     *
     * @since 3.0.0
     */
    public static final @Nullable Tag NULL_TAG = null;

    /**
     * An Audience placeholder that returns a null Tag
     *
     * @param <A> the placeholder supported type
     * @return an empty audience resolver
     * @since 3.0.0
     */
    public static <A extends Audience> AudienceTagResolver<A> emptyAudienceResolver() {
        return (audience, queue, ctx) -> NULL_TAG;
    }

    /**
     * A Relational placeholder that returns a null tag
     *
     * @param <A> the audience type supported
     * @return an empty relational resolver
     * @since 3.0.0
     */
    public static <A extends Audience> RelationalTagResolver<A> emptyRelationalResolver() {
        return (audience, relational, queue, ctx) -> NULL_TAG;
    }

    /**
     * A Global placeholder that returns a null tag
     *
     * @since 3.0.0
     * @return an empty global placeholder
     */
    public static GlobalTagResolver emptyGlobalPlaceholder() {
        return (queue, ctx) -> NULL_TAG;
    }

}
