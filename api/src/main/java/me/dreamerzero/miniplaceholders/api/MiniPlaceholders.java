package me.dreamerzero.miniplaceholders.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import me.dreamerzero.miniplaceholders.api.enums.Platform;
import me.dreamerzero.miniplaceholders.connect.InternalPlatform;
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

    /**
     * Get the platform
     * @return the platform
     * @since 1.0.0
     */
    public static @NotNull Platform getPlatform(){
        return InternalPlatform.platform() == InternalPlatform.PAPER
            ? Platform.PAPER
            : Platform.VELOCITY;
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
    @Contract("null -> fail, !null -> !null")
    public static @NotNull TagResolver getAudiencePlaceholders(final @NotNull Audience audience) {
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
    @Contract("_, null -> fail; null, _ -> fail; !null, !null -> !null")
    public static @NotNull TagResolver getRelationalPlaceholders(final @NotNull Audience audience, final @NotNull Audience otherAudience) {
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
    @Contract("_, null -> fail; null, _ -> fail; !null, !null -> !null")
    public static @NotNull TagResolver getAudienceGlobalPlaceholders(final @NotNull Audience audience) {
        Objects.requireNonNull(audience, () -> "audience cannot be null");
        final TagResolver.Builder builder = TagResolver.builder();

        for(final Expansion expansion : expansions) {
            builder.resolver(expansion.audiencePlaceholders(audience));
            builder.resolver(expansion.globalPlaceholders());
        }

        return builder.build();
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
    @Contract("_, null -> fail; null, _ -> fail; !null, !null -> !null")
    public static @NotNull TagResolver getRelationalGlobalPlaceholders(final @NotNull Audience audience, final @NotNull Audience otherAudience) {
        Objects.requireNonNull(audience, () -> "audience cannot be null");
        Objects.requireNonNull(otherAudience, () -> "otherAudience cannot be null");

        final TagResolver.Builder builder = TagResolver.builder();
        for(final Expansion expansion : expansions) {
            builder.resolver(expansion.audiencePlaceholders(audience));
            builder.resolver(expansion.relationalPlaceholders(audience, otherAudience));
            builder.resolver(expansion.globalPlaceholders());
        }

        return builder.build();
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
    @Deprecated(forRemoval = true, since = "1.1.0")
    @org.jetbrains.annotations.ApiStatus.Internal
    public static void setPlatform(Platform platform){
        throw new UnsupportedOperationException("Cannot set platform twice");
    }
}
