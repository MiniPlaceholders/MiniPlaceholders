package me.dreamerzero.miniplaceholders.api.placeholder;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

/**Audience Placeholder based on an Audience */
public interface AudiencePlaceholder {
    /**
     * A Tag based on an Audience
     * @param audience the audience
     * @param queue the argument queue
     * @param ctx the context
     * @return a Tag
     */
    Tag tag(Audience audience, ArgumentQueue queue, Context ctx);
}
