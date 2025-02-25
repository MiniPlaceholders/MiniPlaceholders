package io.github.miniplaceholders.api.types;

/**
 * Type of Placeholder.
 *
 * @since 3.0.0
 */
public enum PlaceholderType {
  /**
   * The global placeholder type.
   * <br>
   * It does not require any type of audience to operate.
   */
  GLOBAL,
  /**
   * The audience placeholder type.
   * <br>
   * Obtains information from a provided audience.
   */
  AUDIENCE,
  /**
   * The relational placeholder type.
   * <br>
   * Requires 2 audiences to work, these audiences need to be of the same type.
   */
  RELATIONAL
}
