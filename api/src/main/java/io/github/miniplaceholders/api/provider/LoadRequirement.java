package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.types.Platform;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * This class specifies the conditions that must be met
 * in order to load an {@link io.github.miniplaceholders.api.Expansion} from an {@link ExpansionProvider}.
 *
 * @since 3.0.0
 */
@NullMarked
public sealed interface LoadRequirement {

  /**
   * It means that there is no requirement to load an expansion.
   *
   * @return a LoadRequirement that does not perform any validation
   * @since 3.0.0
   */
  static LoadRequirement none() {
    return NoneLoadRequirement.INSTANCE;
  }

  /**
   * Requirement for a complement, whether a plugin or mod, to be present in order for an expansion to be loaded.<br>
   * The ability to provide multiple strings is provided because names may vary on some platforms.
   * For example, Velocity requires plugin IDs to be lowercase, such as {@code miniplaceholders},
   * while Paper can contain uppercase letters, such as {@code MiniPlaceholders}.
   *
   * @param name    the principal name of this complement
   * @param aliases the platform aliases of a complement
   * @return a load requirement that checks for an available complement
   * @since 3.0.0
   */
  static LoadRequirement requiredComplement(final String name, final String... aliases) {
    requireNonNull(name);
    requireNonNull(aliases);
    return new AvailableComplementRequirement(name, aliases);
  }

  /**
   * Multiple requirement, in case an expansion needs to depend on a platform
   * as well as one or more add-ons that are installed.
   *
   * @param requirements the load requirements
   * @return a load requirement of many load requirements
   * @since 3.0.0
   */
  static LoadRequirement allOf(LoadRequirement... requirements) {
    requireNonNull(requirements);
    assert requirements.length != 0;
    return new MultiLoadRequirement(requirements);
  }

  /**
   * Requirement that the current execution be on a specific platform.
   *
   * @param platforms the supported platforms
   * @return a load requirement that checks for a supported platform
   * @since 3.0.0
   */
  static LoadRequirement platform(final Platform... platforms) {
    requireNonNull(platforms);
    assert platforms.length != 0;

    return new PlatformRequirement(platforms);
  }

  /**
   * Requirement that a specific class be present.
   *
   * @param clazz the specified class.
   * @return a load requirement that checks for a present class
   * @since 3.1.0
   */
  static LoadRequirement requiredClass(
      @Pattern("^([a-z_][a-z0-9_]*)(\\.[a-z_][a-z0-9_]*)*(\\.[A-Z][A-Za-z0-9_]*)$") final String clazz
  ) {
    requireNonNull(clazz);
    return new ClassRequirement(clazz);
  }

  /**
   * This requirement checks whether any of the conditions presented are satisfied.
   *
   * @param requirements the requerimients
   * @return a requirement that checks whether a condition is valid
   * @since 3.1.0
   */
  static LoadRequirement anyOf(LoadRequirement... requirements) {
    requireNonNull(requirements);
    assert requirements.length != 0;
    return new AnyOfRequirement(requirements);
  }

  /**
   * NoneLoadRequirement
   *
   * @since 3.0.0
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
   *
   * @param name            the main complement name
   * @param platformAliases the complement aliases
   * @since 3.0.0
   */
  @ApiStatus.Internal
  @NullMarked
  record AvailableComplementRequirement(String name, String... platformAliases) implements LoadRequirement {

    /**
     * Checks if a complement is loaded
     *
     * @param platformComplementTester the checker predicate
     * @return is this expansion should load
     * @since 3.0.0
     */
    public boolean shouldLoad(final Predicate<String> platformComplementTester) {
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
   *
   * @param requirements the load requirements
   * @since 3.0.0
   */
  @ApiStatus.Internal
  @NullMarked
  record MultiLoadRequirement(LoadRequirement... requirements) implements LoadRequirement {
    @Override
    public String toString() {
      return "MultiLoadRequirement{" +
          "requirements=" + Arrays.toString(requirements) +
          '}';
    }
  }

  /**
   * PlatformLoadRequirement
   *
   * @param platforms the platforms supported
   * @since 3.0.0
   */
  @ApiStatus.Internal
  @NullMarked
  record PlatformRequirement(Platform... platforms) implements LoadRequirement {
    @Override
    public String toString() {
      return "PlatformRequirement{" +
          "platforms=" + Arrays.toString(platforms()) +
          '}';
    }
  }

  /**
   * ClassRequirement
   *
   * @param clazz a specified class
   * @since 3.1.0
   */
  @NullMarked
  @ApiStatus.Internal
  record ClassRequirement(String clazz) implements LoadRequirement {
    public boolean shouldLoad() {
      try {
        Class.forName(clazz);
        return true;
      } catch (ClassNotFoundException e) {
        return false;
      }
    }
  }

  /**
   * AnyOfLoadRequirement
   *
   * @param requirements the requirements
   * @since 3.1.0
   */
  @ApiStatus.Internal
  @NullMarked
  record AnyOfRequirement(LoadRequirement... requirements) implements LoadRequirement {
    @Override
    public String toString() {
      return "AnyOfRequirement{" +
          "requirements=" + Arrays.toString(requirements) +
          '}';
    }
  }
}
