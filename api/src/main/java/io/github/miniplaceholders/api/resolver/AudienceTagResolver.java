package io.github.miniplaceholders.api.resolver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

/**Audience Placeholder based on an Audience */
@FunctionalInterface
public interface AudienceTagResolver<A extends Audience> {
    /**
     * A Tag based on an Audience
     * @param audience the audience
     * @param queue the argument queue
     * @param ctx the context
     * @return a Tag
     * @since 1.0.0
     */
    @Nullable Tag tag(
        @NotNull final A audience,
        @NotNull final ArgumentQueue queue,
        @NotNull final Context ctx
    );
}
