package io.github.miniplaceholders.api;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@NullMarked
record SingleAudiencePlaceholder<A extends Audience>(
        @Nullable Class<A> targetClass,
        String key,
        AudiencePlaceholder<A> audiencePlaceholder
) implements TagResolver {

  static <A extends Audience> SingleAudiencePlaceholder<A> single(
          @Nullable Class<A> targetClass,
          String key,
          AudiencePlaceholder<A> placeholder
  ) {
    return new SingleAudiencePlaceholder<>(targetClass, requireNonNull(key), requireNonNull(placeholder));
  }

  @Override
  public @Nullable Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
    final Pointered targetRaw = ctx.target();
    if (targetRaw == null) {
      return null;
    }
    if (targetClass == null || !(targetClass.isInstance(targetRaw))) {
      //noinspection unchecked
      return this.resolveA(name, (A) targetRaw, arguments, ctx);
    }
    final A audience = targetClass.cast(targetRaw);
    return resolveA(name, audience, arguments, ctx);
  }

  private @Nullable Tag resolveA(String key, A audience, ArgumentQueue queue, Context ctx) {
    return this.has(key)
            ? audiencePlaceholder.tag(audience, queue, ctx)
            : null;
  }

  @Override
  public boolean has(String name) {
    return key.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof SingleAudiencePlaceholder<?> that)) return false;
    return that.key.equalsIgnoreCase(this.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}