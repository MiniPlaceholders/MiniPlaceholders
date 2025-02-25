package io.github.miniplaceholders.api.placeholder;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PlaceholderTagResolver(Placeholder... placeholders) implements TagResolver {
  public static final PlaceholderTagResolver EMPTY = new PlaceholderTagResolver();

  @Override
  public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
    @Nullable Tag tag;
    for (final Placeholder placeholder : placeholders) {
      tag = placeholder.resolve(name, arguments, ctx);
      if (tag != null) {
        return tag;
      }
    }
    return null;
  }

  @Override
  public boolean has(final @NotNull String name) {
    for (final Placeholder placeholder : placeholders) {
      if (placeholder.has(name)) {
        return true;
      }
    }
    return false;
  }
}
