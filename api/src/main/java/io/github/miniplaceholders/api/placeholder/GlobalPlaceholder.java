package io.github.miniplaceholders.api.placeholder;

import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record GlobalPlaceholder(@TagPattern String key, GlobalTagResolver resolver) implements Placeholder {
  @Override
  public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) {
    return this.has(name) ? resolver.tag(arguments, ctx) : null;
  }

  @Override
  public boolean has(@NotNull String name) {
    return key.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof GlobalPlaceholder(var oKey, var __)) {
      return Objects.equals(oKey, this.key);
    }
    return false;
  }

  @Override
  public String toString() {
    return "GlobalPlaceholder{" +
            "key='" + key + '\'' +
            '}';
  }
}
