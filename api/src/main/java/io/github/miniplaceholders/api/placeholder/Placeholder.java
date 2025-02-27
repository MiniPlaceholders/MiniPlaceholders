package io.github.miniplaceholders.api.placeholder;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface Placeholder extends TagResolver permits AudiencePlaceholder, GlobalPlaceholder, RelationalPlaceholder {
  String key();
}
