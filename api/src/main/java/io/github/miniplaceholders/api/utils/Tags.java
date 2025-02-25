package io.github.miniplaceholders.api.utils;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

/**
 * Tags utils
 */
public final class Tags {
  private Tags() {
  }

  /**
   * Empty Tag
   *
   * @since 1.0.0
   */
  public static final Tag EMPTY_TAG = Tag.selfClosingInserting(Component.empty());

  /**
   * Null Tag
   *
   * @since 1.1.0
   */
  public static final Tag NULL_TAG = null;

  /**
   * An Audience placeholder that returns a null Tag
   *
   * @since 1.1.0
   */
  public static final AudienceTagResolver<Audience> NULL_AUDIENCE_PLACEHOLDER = (aud, queue, ctx) -> NULL_TAG;

  /**
   * A Relational placeholder that returns a null tag
   *
   * @since 1.1.0
   */
  public static final RelationalTagResolver<Audience> NULL_RELATIONAL_PLACEHOLDER = (aud, relational, queue, ctx) -> NULL_TAG;

}
