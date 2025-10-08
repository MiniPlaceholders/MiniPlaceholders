package io.github.miniplaceholders.api.resolver;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Relational Placeholder based on two Audiences.
 *
 * @param <A> the audiences type
 *
 * @since 3.0.0
 */
@FunctionalInterface
@NullMarked
public interface RelationalTagResolver<A extends Audience> {
  /**
   * A Tag based on two audiences
   *
   * @param audience   the principal audience
   * @param relational the another audience
   * @param queue      the argument queue
   * @param ctx        the context
   * @return a tag
   * @since 3.0.0
   */
  @Nullable
  Tag tag(final A audience, final A relational, final ArgumentQueue queue, final Context ctx);
}
