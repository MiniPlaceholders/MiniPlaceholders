package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.placeholder.RelationalPlaceholder;
import io.github.miniplaceholders.api.relational.RelationalAudience;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@NullMarked
record RelationalAudiencePlaceholder<A extends Audience>(
        @Nullable Class<A> targetClass,
        String key,
        RelationalPlaceholder<A> relationalPlaceholder
) implements TagResolver {

  static <A extends Audience> RelationalAudiencePlaceholder<A> relational(
          @Nullable Class<A> targetClass, String key, RelationalPlaceholder<A> placeholder) {
    return new RelationalAudiencePlaceholder<>(targetClass, requireNonNull(key), requireNonNull(placeholder));
  }

  @Override
  public @Nullable Tag resolve(String name, ArgumentQueue arguments, Context ctx) {
    final Pointered targetRaw = ctx.target();
    if (targetRaw == null) {
      return null;
    }
    if (!(targetRaw instanceof RelationalAudience<?>(Audience a, Audience b))) {
      return null;
    }
    if (targetClass == null) {
      //noinspection unchecked
      return this.resolveA(name, (A)a, (A)b, arguments, ctx);
    }
    if (!targetClass.isInstance(a) || !targetClass.isInstance(b)) {
      return null;
    }
    final A audience = targetClass.cast(a);
    final A relationalAudience = targetClass.cast(b);
    return this.resolveA(name, audience, relationalAudience, arguments, ctx);
  }

  private @Nullable Tag resolveA(
          String name,
          A audience,
          A relationalAudience,
          ArgumentQueue arguments,
          Context ctx
  ) {
    return this.has(name)
            ? relationalPlaceholder.tag(audience, relationalAudience, arguments, ctx)
            : null;
  }

  @Override
  public boolean has(@NotNull String name) {
    return key.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof final RelationalAudiencePlaceholder<?> that)) return false;
    return that.key.equalsIgnoreCase(this.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}