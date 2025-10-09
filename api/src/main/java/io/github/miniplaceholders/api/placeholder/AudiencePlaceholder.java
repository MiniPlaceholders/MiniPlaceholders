package io.github.miniplaceholders.api.placeholder;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A placeholder that can be applied based on an audience of the specified type.
 *
 * @param targetClass the type of audience from which this placeholder can obtain information
 * @param key the full placeholder key
 * @param name the name of this placeholder
 * @param resolver the object responsible for obtaining audience information
 * @param <A> the parametrized type
 */
public record AudiencePlaceholder<A extends Audience>(
        @Nullable Class<A> targetClass,
        @TagPattern String key,
        String name,
        AudienceTagResolver<A> resolver
) implements Placeholder {

  /**
   * Creates a new AudiencePlaceholder.
   *
   * @param targetClass the type of audience from which this placeholder can obtain information
   * @param key the placeholder key
   * @param name the name of this placeholder
   * @param resolver the object responsible for obtaining audience information
   * @return a new AudiencePlaceholder
   * @param <A> the parametrized type
   */
  public static <A extends Audience> AudiencePlaceholder<A> single(
          @Nullable Class<A> targetClass,
          @TagPattern String key,
          String name,
          AudienceTagResolver<A> resolver
  ) {
    return new AudiencePlaceholder<>(targetClass, requireNonNull(key), requireNonNull(name), requireNonNull(resolver));
  }

  @Override
  public @Nullable Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
    final Pointered targetRaw = ctx.target();
    if (targetRaw == null) {
      return null;
    }
    if (targetClass == null) {
      //noinspection unchecked
      return this.resolveA(name, (A) targetRaw, arguments, ctx);
    }
    final @Nullable A audience = forwardingFilter(targetRaw);
    if (audience == null) {
      return null;
    }
    return resolveA(name, audience, arguments, ctx);
  }

  private @Nullable Tag resolveA(String key, A audience, ArgumentQueue queue, Context ctx) {
    return this.has(key)
            ? resolver.tag(audience, queue, ctx)
            : null;
  }

  @SuppressWarnings("OverrideOnly")
  private @Nullable A forwardingFilter(final Pointered source) {
    //noinspection DataFlowIssue
    if (targetClass.isInstance(source)) {
      return targetClass.cast(source);
    }
    if (source instanceof final ForwardingAudience.Single forward) {
      return forwardingFilter(forward.audience());
    }
    return null;
  }

  @Override
  public boolean has(String name) {
    return key.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof AudiencePlaceholder<?> that)) return false;
    return that.key.equalsIgnoreCase(this.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return "AudiencePlaceholder{" +
            "key='" + key + '\'' +
            '}';
  }
}