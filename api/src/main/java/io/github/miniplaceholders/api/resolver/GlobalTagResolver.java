package io.github.miniplaceholders.api.resolver;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
  Tag tag(@NonNull ArgumentQueue queue, @NonNull Context context);
}
