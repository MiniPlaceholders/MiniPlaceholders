package io.github.miniplaceholders.api.placeholder;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Placeholder extends TagResolver {
  String key();
}
