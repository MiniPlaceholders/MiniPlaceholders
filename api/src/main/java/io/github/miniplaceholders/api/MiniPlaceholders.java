package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.types.PlaceholderType;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.api.types.RelationalAudience;
import io.github.miniplaceholders.connect.InternalPlatform;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.miniplaceholders.api.utils.Resolvers.applyIfNotEmpty;

/**
 * MiniPlaceholders, a component-based placeholders API.
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
     * Get the platform on which MiniPlaceholders is running.
     *
     * @return the platform
     * @since 3.0.0
     */
    public static Platform platform() {
        return switch (InternalPlatform.platform()) {
            case PAPER -> Platform.PAPER;
            case VELOCITY -> Platform.VELOCITY;
            case FABRIC -> Platform.FABRIC;
            case SPONGE -> Platform.SPONGE;
        };
    }

    /**
     * Get the global placeholders
     *
     * <pre>TagResolver resolver = MiniPlaceholders.globalPlaceholders();
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, resolver);</pre>
     *
     * @return global placeholders independent of any audience
     * @see TagResolver
     * @since 3.0.0
     */
    public static TagResolver globalPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            final TagResolver resolver = expansion.globalPlaceholders();
            applyIfNotEmpty(resolver, builder);
        }
        return builder.build();
    }

    /**
     * Gets the TagResolver that can get data from an Audience.
     * <br>
     * The audience is provided at the time of parsing from the respective MiniMessage instance.
     *
     * <pre>TagResolver resolver = MiniPlaceholders.audiencePlaceholders();
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, {@link Audience}, resolver);
     * </pre>
     *
     * @return {@link TagResolver} with placeholders based on an audience
     * @since 3.0.0
     */
    public static TagResolver audiencePlaceholders() {
        final TagResolver.Builder resolvers = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            final TagResolver resolver = expansion.audiencePlaceholders();
            applyIfNotEmpty(resolver, resolvers);
        }
        return resolvers.build();
    }

    /**
     * Get the relational placeholders based on two audiences
     * <br>
     * The audiences are provided at the time of parsing
     * from the respective MiniMessage instance through the use of a {@link RelationalAudience}.
     *
     * <pre>{@code
     *      TagResolver resolver = MiniPlaceholders.relationalPlaceholders();
     *      Component parsed = MiniMessage.miniMessage().deserialize(@link String, {@link RelationalAudience}, resolver);
     * }</pre>
     *
     * @return placeholders based on two audiences
     * @since 3.0.0
     * @see RelationalAudience
     */
    public static TagResolver relationalPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(expansion.relationalPlaceholders(), builder);
        }

        return builder.build();
    }

    /**
     * Get a TagResolver that can obtain data based on a relationship of 2 audiences
     * and at the same time from the main audience and global placeholders.
     * <br>
     * The audience is provided at the time of parsing from the respective MiniMessage instance.
     *
     * <pre>
     * TagResolver resolver = MiniPlaceholders.audienceGlobalPlaceholders();
     * TagResolver resolver2 = TagResolver.resolver(
     *  MiniPlaceholders.audienceGlobalPlaceholders(),
     *  MiniPlaceholders.globalPlaceholders()
     * );
     * // This two resolvers returns the same TagResolver
     * assertEquals(resolver, resolver2);
     * Component parsed = MiniMessage.miniMessage().deserialize({@link String}, {@link Audience}, resolver);
     * </pre>
     *
     * @return {@link TagResolver} with placeholders based on an audience and the global placeholders
     * @since 1.1.0
     */
    public static TagResolver audienceGlobalPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();

        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(expansion.audiencePlaceholders(), builder);
            applyIfNotEmpty(expansion.globalPlaceholders(), builder);
        }

        return builder.build();
    }

    /**
     * Get the relational placeholders based on two audiences, based on the first audience,
     * and the global placeholders
     *
     * <pre>
     * TagResolver resolver = MiniPlaceholders.relationalGlobalPlaceholders();
     * TagResolver resolver2 = TagResolver.resolver(
     *  MiniPlaceholders.relationalPlaceholders(),
     *  MiniPlaceholders.audiencePlaceholders(),
     *  MiniPlaceholders.globalPlaceholders()
     * );
     * // This methods should return the same TagResolver
     * assertEquals(resolver, resolver2);
     * Component messageParsed = MiniMessage.miniMessage().deserialize({@link String}, {@link RelationalAudience}, resolver);
     * </pre>
     *
     * @return the placeholders based on two audiences, placeholders based on the first audience and the global placeholders
     * @since 3.0.0
     * @apiNote In the case of audience placeholders, the audience to be used will be the {@link RelationalAudience#audience()}
     * @see RelationalAudience
     */
    public static TagResolver relationalGlobalPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        for (final Expansion expansion : expansions) {
            applyIfNotEmpty(expansion.audiencePlaceholders(), builder);
            applyIfNotEmpty(expansion.relationalPlaceholders(), builder);
            applyIfNotEmpty(expansion.globalPlaceholders(), builder);
        }

        return builder.build();
    }

    /**
     * Get a TagResolver based on the desired placeholder type.
     * <br>
     * {@link PlaceholderType#GLOBAL} will return {@link #globalPlaceholders()}
     * <br>
     * {@link PlaceholderType#AUDIENCE} will return {@link #audiencePlaceholders()}
     * <br>
     * {@link PlaceholderType#RELATIONAL} will return {@link #relationalPlaceholders()}
     *
     * @param type the desired type
     * @return the TagResolvers from the respective type
     * @see PlaceholderType
     */
    public static TagResolver placeholdersByType(final PlaceholderType type) {
        return switch (type) {
            case GLOBAL -> globalPlaceholders();
            case AUDIENCE -> audiencePlaceholders();
            case RELATIONAL -> relationalPlaceholders();
        };
    }

    /**
     * Get the amount of expansion registered.
     *
     * @return the amount of expansions registered
     * @since 3.0.0
     */
    public static int expansionCount(){
        return expansions.size();
    }

    /**
     * Get a specific expansion by name
     * <p>The name of each expansion is set when the expansion is created</p>
     * <pre>
     *     Expansion expansion = Expansion.builder("example").build();
     *     expansion.register();
     *
     *     assertThat(MiniPlaceholders.expansionByName("example")).isNotNull();
     * </pre>
     *
     * @param name the name of the required expansion
     * @return the required expansion, if not present, will return null
     * @see Expansion#builder(String)
     * @since 3.0.0
     */
    public static @Nullable Expansion expansionByName(final String name) {
        for (final Expansion expansion : expansions) {
            if (Objects.equals(expansion.name(), name)) {
                return expansion;
            }
        }
        return null;
    }

    /**
     * Obtain all available registered expansions
     *
     * @return all available registered expansions
     * @since 3.0.0
     */
    @Unmodifiable
    public static Collection<Expansion> expansionsAvailable() {
        return Collections.unmodifiableSet(expansions);
    }
}
