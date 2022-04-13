package me.dreamerzero.miniplaceholders.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.miniplaceholders.api.enums.Platform;
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
 * @since 1.0.0
 */
public final class MiniPlaceholders {
    private MiniPlaceholders(){}
    static final Set<Expansion> expansions = Collections.synchronizedSet(new HashSet<>());
    private static Platform placeholdersPlatform;

    /**
     * Get the platform
     * @return the platform
     * @since 1.0.0
     */
    public static Platform getPlatform(){
        return placeholdersPlatform;
    }

    /**
     * Get the global placeholders
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getGlobalPlaceholders();
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @return global placeholders independient of any audience
     * @see TagResolver
     * @since 1.0.0
     */
    public static @NotNull TagResolver getGlobalPlaceholders() {
        final TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.globalPlaceholders()));

        return resolvers.build();
    }

    /**
     * Get the TagResolver based on an Audience
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getAudiencePlaceholders({@link Audience});
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @param audience the audience
     * @return {@link TagResolver} with placeholders based on an audience
     * @since 1.0.0
     */
    public static @NotNull TagResolver getAudiencePlaceholders(@NotNull final Audience audience) {
        Objects.requireNonNull(audience, () -> "audience cannot be null");

        final TagResolver.Builder resolvers = TagResolver.builder();
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
     * @since 1.0.0
     */
    public static @NotNull TagResolver getRelationalPlaceholders(@NotNull final Audience audience, @NotNull final Audience otherAudience) {
        Objects.requireNonNull(audience, () -> "audience cannot be null");
        Objects.requireNonNull(otherAudience, () -> "otherAudience cannot be null");

        final TagResolver.Builder resolvers = TagResolver.builder();
        expansions.forEach(exp -> resolvers.resolver(exp.relationalPlaceholders(audience, otherAudience)));

        return resolvers.build();
    }

    /**
     * Get the TagResolver based on an Audience and the global placeholders
     *
     * <pre>
     * TagResolver resolver = MiniPlaceholders.getAudienceGlobalPlaceholders({@link Audience});
     * TagResolver resolver2 = TagResolver.resolver(
     *  MiniPlaceholders.getAudienceGlobalPlaceholders({@link Audience}),
     *  MiniPlaceholders.getGlobalPlaceholders()
     * );
     * // This two resolvers returns the same TagResolver
     * assertEquals(resolver, resolver2);
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);
     * </pre>
     *
     * @param audience the audience
     * @return {@link TagResolver} with placeholders based on an audience and the global placeholders
     * @since 1.1.0
     */
    public static @NotNull TagResolver getAudienceGlobalPlaceholders(@NotNull final Audience audience) {
        return TagResolver.resolver(
            MiniPlaceholders.getAudiencePlaceholders(audience),
            MiniPlaceholders.getGlobalPlaceholders()
        );
    }

    /**
     * Get the relationals placeholders based on two audiences, based on the first audience,
     * and the global placeholders
     *
     * <pre>
     * TagResolver resolver = MiniPlaceholders.getRelationalGlobalPlaceholders({@link Audience}, {@link Audience});
     * TagResolver resolver2 = TagResolver.resolver(
     *  MiniPlaceholders.getRelationalPlaceholders(audience1, audience2),
     *  MiniPlaceholders.getAudiencePlaceholders(audience1),
     *  MiniPlaceholders.getGlobalPlaceholders()
     * );
     * // This methods should return the same TagResolver
     * assertEquals(resolver, resolver2);
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);
     * </pre>
     *
     * @param audience an audience
     * @param otherAudience another audience
     * @return the placeholders based on two audiences, placeholders based on the first audience and the global placeholders
     * @since 1.1.0
     */
    public static @NotNull TagResolver getRelationalGlobalPlaceholders(@NotNull final Audience audience, @NotNull final Audience otherAudience) {
        return TagResolver.resolver(
            MiniPlaceholders.getAudiencePlaceholders(audience),
            MiniPlaceholders.getGlobalPlaceholders(),
            MiniPlaceholders.getRelationalPlaceholders(audience, otherAudience)
        );
    }

    /**
     * Get the ammount of expansion registered
     * @return the ammount of expansions registered
     * @since 1.0.0
     */
    public static int getExpansionCount(){
        return expansions.size();
    }

    /**
     * Set the platform
     * @param platform the platform
     * @deprecated dont use this, INTERNAL
     * @since 1.0.0
     */
    @Deprecated(forRemoval = false)
    @org.jetbrains.annotations.ApiStatus.Internal
    public static void setPlatform(Platform platform){
        if(placeholdersPlatform != null) throw new RuntimeException("Cannot set platform twice");
        placeholdersPlatform = platform;
    }
}
