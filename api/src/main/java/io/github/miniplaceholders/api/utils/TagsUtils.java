package io.github.miniplaceholders.api.utils;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import io.github.miniplaceholders.api.placeholder.RelationalPlaceholder;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

/**Tags utils */
public final class TagsUtils {
    private TagsUtils(){}

    /**
     * Empty Tag
     * @since 1.0.0
     */
    public static final Tag EMPTY_TAG = Tag.selfClosingInserting(Component.empty());

    /**
     * Null Tag
     * @since 1.1.0
     */
    public static final Tag NULL_TAG = null;

    /**
     * An Audience placeholder that returns a null Tag
     * @since 1.1.0
     */
    public static final AudiencePlaceholder NULL_AUDIENCE_PLACEHOLDER = (aud, queue, ctx) -> NULL_TAG;

    /**
     * A Relational placeholder that returns a null tag
     * @since 1.1.0
     */
    public static final RelationalPlaceholder NULL_RELATIONAL_PLACEHOLDER = (aud, otheraud, queue, ctx) -> NULL_TAG;

    /**
     * Creates a new Tag based on a String
     * @param string the string
     * @return a new basic self closing Tag of a string
     * @since 1.1.0
     */
    @Deprecated
    public static @NotNull Tag staticTag(@NotNull String string) {
        return Tag.selfClosingInserting(Component.text(string));
    }

    /**
     * Creates a new basic Tag based on a Component
     * @param component the component
     * @return a new basic self closing Tag of a component
     * @since 1.1.0
     */
    @Deprecated
    public static @NotNull Tag staticTag(@NotNull Component component) {
        return Tag.selfClosingInserting(component);
    }

}
