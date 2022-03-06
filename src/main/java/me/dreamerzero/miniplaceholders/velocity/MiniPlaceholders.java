package me.dreamerzero.miniplaceholders.velocity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;


/**
 * MiniPlaceholders
 *
 * <p>This class allows you to obtain the {@link TagResolver}
 * that other plugins provide based on single {@link Audience},
 * 2-audience or global placeholders</p>
 *
 * @author 4drian3d
 * @see TagResolver
 * @see Expansion
 */
public final class MiniPlaceholders {
    private MiniPlaceholders(){}
    protected static final Set<Expansion> expansions = Collections.synchronizedSet(new HashSet<>());

    /**
     * Get the global placeholders
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getGlobalPlaceholders();
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @return global placeholders independient of any audience
     * @see TagResolver
     */
    public static TagResolver getGlobalPlaceholders() {
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.globalPlaceholders()));

        return resolvers.build();
    }

    /**
     * Get the TagResolver based on an Audience
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getAudiencerPlaceholders({@link Audience});
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @since 1.0.0
     * @param audience the audience
     * @return {@link TagResolver} with placeholders based on an audience
     */
    public static TagResolver getAudiencePlaceholders(Audience audience) {
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.audiencePlaceholders(audience)));

        return resolvers.build();
    }

    /**
     * Get the relationals placeholders based on two audiences
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getRelationalPlaceholders({@link Audience}, {@link Audience});
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @param audience an audience
     * @param otherAudience another audience
     * @return placeholders based on two audiences
     */
    public static TagResolver getRelationalPlaceholders(Audience audience, Audience otherAudience) {
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.relationalPlaceholders(audience, otherAudience)));

        return resolvers.build();
    }
}
