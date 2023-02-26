package io.github.miniplaceholders.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

/**Relational Placeholder based on two Audiences*/
@FunctionalInterface
public interface RelationalPlaceholder {
    /**
     * A Tag based on two audiences
     * @param audience the principal audience
     * @param otherAudience the another audience
     * @param queue the argument queue
     * @param ctx the context
     * @return a tag
     * @since 1.0.0
     */
    @Nullable Tag tag(
        @NotNull final Audience audience,
        @NotNull final Audience otherAudience,
        @NotNull final ArgumentQueue queue,
        @NotNull final Context ctx
    );
}
