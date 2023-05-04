package io.github.miniplaceholders.api;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.github.miniplaceholders.api.enums.Platform;
import org.jetbrains.annotations.NotNull;

import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

import static io.github.miniplaceholders.api.utils.Resolvers.applyIfNotEmpty;
import static java.util.Objects.requireNonNull;

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
    private MiniPlaceholders() {}
    static final Set<Expansion> expansions = ConcurrentHashMap.newKeySet();

    /**
     * Get the platform
     * @return the platform
     * @since 1.0.0
     */
    public static @NotNull Platform getPlatform() {
        return switch (InternalPlatform.platform()) {
            case PAPER -> Platform.PAPER;
            case VELOCITY -> Platform.VELOCITY;
            case KRYPTON -> Platform.KRYPTON;
            case FABRIC -> Platform.FABRIC;
            case SPONGE -> Platform.SPONGE;
        };
    }

    /**
     * Get the global placeholders
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getGlobalPlaceholders();
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @return global placeholders independent of any audience
     * @see TagResolver
     * @since 1.0.0
     */
    public static @NotNull TagResolver getGlobalPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            final TagResolver resolver = expansion.globalPlaceholders();
            if (resolver != TagResolver.empty())
                builder.resolver(resolver);
        }
        return builder.build();
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
    public static @NotNull TagResolver getAudiencePlaceholders(final @NotNull Audience audience) {
        requireNonNull(audience, "audience cannot be null");

        final TagResolver.Builder resolvers = TagResolver.builder();
        for (Expansion expansion : expansions) {
            final TagResolver resolver = expansion.audiencePlaceholders(audience);
            applyIfNotEmpty(resolver, resolvers);
        }
        return resolvers.build();
    }

    /**
     * Get the relational placeholders based on two audiences
     *
     * <pre>TagResolver resolver = MiniPlaceholders.getRelationalPlaceholders({@link Audience}, {@link Audience});
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @param audience an audience
     * @param otherAudience another audience
     * @return placeholders based on two audiences
     * @since 1.0.0
     */
    public static @NotNull TagResolver getRelationalPlaceholders(final @NotNull Audience audience, final @NotNull Audience otherAudience) {
        requireNonNull(audience, "audience cannot be null");
        requireNonNull(otherAudience, "otherAudience cannot be null");

        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(
                expansion.relationalPlaceholders(audience, otherAudience),
                builder
            );
        }

        return builder.build();
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
    public static @NotNull TagResolver getAudienceGlobalPlaceholders(final @NotNull Audience audience) {
        requireNonNull(audience, "audience cannot be null");
        final TagResolver.Builder builder = TagResolver.builder();

        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(expansion.audiencePlaceholders(audience), builder);
            applyIfNotEmpty(expansion.globalPlaceholders(), builder);
        }

        return builder.build();
    }

    /**
     * Get the relational placeholders based on two audiences, based on the first audience,
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
    public static @NotNull TagResolver getRelationalGlobalPlaceholders(final @NotNull Audience audience, final @NotNull Audience otherAudience) {
        requireNonNull(audience, "audience cannot be null");
        requireNonNull(otherAudience, "otherAudience cannot be null");

        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(expansion.audiencePlaceholders(audience), builder);
            applyIfNotEmpty(expansion.relationalPlaceholders(audience, otherAudience), builder);
            applyIfNotEmpty(expansion.globalPlaceholders(), builder);
        }

        return builder.build();
    }

    /**
     * Get the amount of expansion registered
     * @return the amount of expansions registered
     * @since 1.0.0
     */
    public static int getExpansionCount(){
        return expansions.size();
    }

    /**
     * Get a specific expansion by name
     * <p>The name of each expansion is set when the expansion is created</p>
     * <pre>
     *     Expansion expansion = Expansion.builder("example").build();
     *     expansion.register();
     *
     *     assertThat(MiniPlaceholders.getExpansionByName("example")).isNotNull();
     * </pre>
     * @param name the name of the required expansion
     * @return the required expansion, if not present, will return null
     * @see Expansion#builder(String)
     * @since 2.1.0
     */
    public static @Nullable Expansion getExpansionByName(final @NotNull String name) {
        for (final Expansion expansion : expansions) {
            if (Objects.equals(expansion.name(), name)) {
                return expansion;
            }
        }
        return null;
    }
}
