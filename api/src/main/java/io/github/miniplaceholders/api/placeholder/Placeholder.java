package io.github.miniplaceholders.api.placeholder;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

/**
 * Base class of all Placeholders
 */
@NullMarked
public sealed interface Placeholder extends TagResolver permits AudiencePlaceholder, GlobalPlaceholder, RelationalPlaceholder {
  /**
   * The key of this placeholder.<br>
   * For example, the placeholder <pre>{@code <expansion_placeholder}</pre>
   * will have the key <code>expansion_placeholder</code>
   *
   * @return this placeholder's key
   */
  String key();

  /**
   * The key of this placeholder.<br>
   * For example, the placeholder <pre>{@code <expansion_placeholder}</pre>
   * will have the key <code>placeholder</code>
   *
   * @return this placeholder's name
   */
  String name();
}
