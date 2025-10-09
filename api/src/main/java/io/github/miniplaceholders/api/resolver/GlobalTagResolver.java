package io.github.miniplaceholders.api.resolver;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.Nullable;

/**
 * A GlobalResolver is responsible for providing information regardless of additional conditions such as an audience.
 */
@FunctionalInterface
public interface GlobalTagResolver {
  /**
   * A global tag.
   *
   * @param queue the argument queue
   * @param context the parsing context
   * @return a tag
   * @since 3.0.0
   */
  @Nullable
  Tag tag(ArgumentQueue queue, Context context);
}
