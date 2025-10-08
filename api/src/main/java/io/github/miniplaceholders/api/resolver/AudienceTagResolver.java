package io.github.miniplaceholders.api.resolver;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Audience Placeholder based on an Audience
 *
 * @param <A> the audience type
 */
@FunctionalInterface
@NullMarked
public interface AudienceTagResolver<A extends Audience> {
    /**
     * A Tag based on an Audience
     * @param audience the audience
     * @param queue the argument queue
     * @param ctx the context
     * @return a Tag
     * @since 3.0.0
     */
    @Nullable
    Tag tag(final A audience, final ArgumentQueue queue, final Context ctx);
}
