package me.dreamerzero.miniplaceholders.velocity;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import org.jetbrains.annotations.ApiStatus;

import org.slf4j.Logger;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * MiniPlaceholderAPI
 *
 * <p>This class allows you to obtain the {@link TagResolver}
 * that other plugins provide based on single {@link Audience},
 * 2-audience or global placeholders</p>
 *
 * @author 4drian3d
 * @see TagResolver
 * @see ExpansionImpl
 */
public final class MiniPlaceholders {
    private final Logger logger;

    protected static final Set<ExpansionImpl> expansions = Sets.newConcurrentHashSet();

    @Inject
    @ApiStatus.Internal
    public MiniPlaceholders(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    @ApiStatus.Internal
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting MiniPlaceholderAPI");
    }

    /**
     * Get the TagResolver based on an Audience
     *
     * <pre>TagResolver resolver = MiniPlaceholderAPI.getAudiencerPlaceholders({@link Audience});
     * Component messageParsed = MiniMessage.deserialize(String message, resolver);</pre>
     *
     * @since 1.0.0
     * @param audience the audience
     * @return {@link TagResolver} with placeholders based on an audience
     */
    public static TagResolver getAudiencePlaceholders(Audience audience){
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.getAudiencePlaceholders(audience)));

        return resolvers.build();
    }

    /**
     * Get the global placeholders
     *
     * <pre>TagResolver resolver = MiniPlaceholderAPI.getGlobalPlaceholders();
     * Component messageParsed = MiniMessage.deserialize(String message, resolver);</pre>
     *
     * @return global placeholders independient of any audience
     * @see TagResolver
     */
    public static TagResolver getGlobalPlaceholders(){
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.getGlobalPlaceholders()));

        return resolvers.build();
    }

    /**
     * Get the relationals placeholders based on two audiences
     *
     * <pre>TagResolver resolver = MiniPlaceholderAPI.getRelationalPlaceholders({@link Audience}, {@link Audience});
     * Component messageParsed = MiniMessage.deserialize(String message, resolver);</pre>
     *
     * @param audience an audience
     * @param otherAudience another audience
     * @return placeholders based on two audiences
     */
    public static TagResolver getRelationalPlaceholders(Audience audience, Audience otherAudience){
        TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.getRelationalPlaceholders(audience, otherAudience)));

        return resolvers.build();
    }
}
