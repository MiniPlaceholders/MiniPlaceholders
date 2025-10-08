package io.github.miniplaceholders.api.utils;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Checks required for the correct operation of the expansions
 *
 * @since 1.0.0
 */
@NullMarked
public final class Conditions {
  private Conditions() {
  }

  /**
   * Checks if a string is null or empty or consists of whitespace characters only.
   *
   * @param string the string
   * @param name   the name supplier
   * @return the string if it is not null or empty
   * @throws IllegalStateException if the provided string is empty
   * @since 1.0.0
   */
  public static String nonNullOrEmptyString(final @Nullable String string, final Supplier<String> name) {
    if (string == null)
      throw new NullPointerException(name.get() + " cannot be null");
    if (string.isBlank())
      throw new IllegalStateException(name.get() + "cannot be an empty string");
    return string;
  }

  /**
   * Checks if a string is null or empty or consists of whitespace characters only.
   *
   * @param string the string
   * @param name   the name
   * @return the string if it is not null or empty
   * @throws IllegalStateException if the provided string is empty
   * @since 2.2.0
   */
  public static String nonNullOrEmptyString(final @Nullable String string, final String name) {
    if (string == null)
      throw new NullPointerException(name + " cannot be null");
    if (string.isBlank())
      throw new IllegalStateException(name + "cannot be an empty string");
    return string;
  }

  /**
   * Check if a string is empty or consists of whitespace characters only.
   *
   * @param string the string
   * @param reason the reason supplier
   * @return the string if it not empty
   * @throws IllegalStateException if the provided string is empty
   * @since 1.0.0
   */
  public static String nonEmptyString(final String string, final Supplier<String> reason) {
    if (string.isBlank()) throw new IllegalStateException(reason.get());
    return string;
  }

  /**
   * Check if a string is empty or consists of whitespace characters only.
   *
   * @param string the string
   * @param reason the reason
   * @return the string if it not empty
   * @throws IllegalStateException if the provided string is empty
   * @since 2.2.0
   */
  public static String nonEmptyString(final String string, final String reason) {
    if (string.isBlank()) throw new IllegalStateException(reason);
    return string;
  }

}
