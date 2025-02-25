package io.github.miniplaceholders.api.utils;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jspecify.annotations.NonNull;

/**
 * Tags basic instances.
 *
 * @since 3.0.0
 */
public final class Tags {
  private Tags() {
  }

  /**
   * Empty Tag
   *
   * @since 3.0.0
   */
  public static final Tag EMPTY_TAG = Tag.selfClosingInserting(Component.empty());

  /**
   * Null Tag
   *
   * @since 3.0.0
   */
  public static final Tag NULL_TAG = null;

  /**
   * An Audience placeholder that returns a null Tag
   *
   * @since 3.0.0
   */
  public static <A extends Audience> AudienceTagResolver<@NonNull A> emptyAudienceResolver() {
    return (audience, queue, ctx) -> NULL_TAG;
  }
  /**
   * A Relational placeholder that returns a null tag
   *
   * @since 3.0.0
   */
  public static <A extends Audience> RelationalTagResolver<@NonNull A> emptyRelationalResolver() {
    return (audience, relational, queue, ctx) -> NULL_TAG;
  }

  /**
   * A Global placeholder that returns a null tag
   *
   * @since 3.0.0
   */
  private static final GlobalTagResolver EMPTY_GLOBAL_PLACEHOLDER = (queue, ctx) -> NULL_TAG;

  public static GlobalTagResolver emptyGlobalPlaceholder() {
    return EMPTY_GLOBAL_PLACEHOLDER;
  }

}
