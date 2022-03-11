package me.dreamerzero.miniplaceholders.api.placeholder;

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
     */
    Tag tag(Audience audience, Audience otherAudience, ArgumentQueue queue, Context ctx);
}
