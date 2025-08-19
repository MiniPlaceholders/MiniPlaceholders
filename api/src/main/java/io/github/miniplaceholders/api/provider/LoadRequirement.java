package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.types.Platform;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * This class specifies the conditions that must be met
 * in order to load an {@link io.github.miniplaceholders.api.Expansion} from an {@link ExpansionProvider}.
 */
public sealed interface LoadRequirement {

    /**
     * It means that there is no requirement to load an expansion.
     * @return a LoadRequirement that does not perform any validation
     */
    static LoadRequirement none() {
        return NoneLoadRequirement.INSTANCE;
    }

    /**
     * Requirement for a complement, whether a plugin or mod, to be present in order for an expansion to be loaded.<br>
     * The ability to provide multiple strings is provided because names may vary on some platforms.
     * For example, Velocity requires plugin IDs to be lowercase, such as {@code miniplaceholders},
     * while Paper can contain uppercase letters, such as {@code MiniPlaceholders}.
     * @param name the principal name of this complement
     * @param aliases the platform aliases of a complement
     * @return a load requirement that checks for an available complement
     */
    static LoadRequirement requiredComplement(final String name, String... aliases) {
        requireNonNull(name);
        return new AvailableComplementRequirement(name, aliases);
    }

    /**
     * Multiple requirement, in case an expansion needs to depend on a platform
     * as well as one or more add-ons that are installed.
     *
     * @param requirements the load requirements
     * @return a load requirement of many load requirements
     */
    static LoadRequirement allOf(LoadRequirement... requirements) {
        requireNonNull(requirements);
        assert requirements.length != 0;
        return new MultiLoadRequirement(requirements);
    }

    /**
     * Requirement that the current execution be on a specific platform.
     * @param platforms the supported platforms
     * @return a load requirement that checks for a supported platform
     */
    static LoadRequirement platform(Platform... platforms) {
        requireNonNull(platforms);
        assert platforms.length != 0;

        return new PlatformRequirement(platforms);
    }

    /**
     * NoneLoadRequirement
     */
    @ApiStatus.Internal
    enum NoneLoadRequirement implements LoadRequirement {
        /**
         * NoneLoadRequirement instance
         */
        INSTANCE
    }

    /**
     * AvailableComplementRequirement
     * @param name the main complement name
     * @param platformAliases the complement aliases
     */
    @ApiStatus.Internal
    @NullMarked
    record AvailableComplementRequirement(String name, String... platformAliases) implements LoadRequirement {

        /**
         * Checks if a complement is loaded
         * @param platformComplementTester the checker predicate
         * @return is this expansion should load
         */
        public boolean shouldLoad(Predicate<String> platformComplementTester) {
            if (platformComplementTester.test(name)) {
                return true;
            }
            for (String alias : platformAliases) {
                if (platformComplementTester.test(alias)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "AvailableComplementRequirement{" +
                    "complement='" + name + '\'' +
                    '}';
        }
    }

    /**
     * MultiLoadRequirement
     * @param requirements the load requirements
     */
    @ApiStatus.Internal
    @NullMarked
    record MultiLoadRequirement(LoadRequirement... requirements) implements LoadRequirement {
    }

    /**
     * PlatformLoadRequirement
     * @param platforms the platforms supported
     */
    @ApiStatus.Internal
    @NullMarked
    record PlatformRequirement(Platform... platforms) implements LoadRequirement, Examinable {
        @Override
        public String toString() {
            final String platforms = Stream.of(this.platforms)
                    .map(Platform::name)
                    .collect(Collectors.joining(", "));
            return "PlatformRequirement{" +
                    "platforms=" + platforms +
                    '}';
        }
    }
}
