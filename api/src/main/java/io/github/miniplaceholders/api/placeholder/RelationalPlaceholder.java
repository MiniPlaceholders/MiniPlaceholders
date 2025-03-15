package io.github.miniplaceholders.api.placeholder;

import io.github.miniplaceholders.api.types.RelationalAudience;
import io.github.miniplaceholders.api.resolver.RelationalTagResolver;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Relational Placeholder.
 * <br>
 * This placeholder is able to obtain data based on a relationship between 2 audiences
 * using a type-safe filter and a relational resolver.
 *
 * @param targetClass the target class, can be null
 * @param key the key of the entire placeholder
 * @param resolver the relational resolver,
 *                 in charge of obtaining data based on a relationship of 2 audiences
 * @param <A> the audience type, if not specified, will be a generic {@linkplain Audience}
 * @see RelationalTagResolver
 * @apiNote The {@link #key()} method returns the full name of the placeholder,
 * for example, if the placeholder is {@code <server_name>},
 * the {@linkplain #key()} method will return {@code server_name}.
 */
@NullMarked
public record RelationalPlaceholder<A extends Audience>(
        @Nullable Class<A> targetClass,
        @TagPattern String key,
        RelationalTagResolver<A> resolver
) implements Placeholder {

  /**
   * Static method capable of creating an instance of a relational placeholder.
   *
   * @param targetClass the target class, can be null
   * @param key the key of the entire placeholder
   * @param resolver the relational resolver,
   *                 in charge of obtaining data based on a relationship of 2 audiences
   * @return a new RelationalPlaceholder
   * @param <A> the audience type, if not specified, will be a generic {@linkplain Audience}
   * @since 3.0.0
   */
  public static <A extends Audience> RelationalPlaceholder<A> relational(
          @Nullable Class<A> targetClass,
          @TagPattern String key,
          RelationalTagResolver<A> resolver
  ) {
    return new RelationalPlaceholder<>(targetClass, requireNonNull(key), requireNonNull(resolver));
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
    final @Nullable A audience = forwardingFilter(a);
    if (audience == null) {
      return null;
    }
    final @Nullable A relationalAudience = forwardingFilter(b);
    if (relationalAudience == null) {
      return null;
    }
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
            ? resolver.tag(audience, relationalAudience, arguments, ctx)
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
  public boolean has(@NotNull String name) {
    return key.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof final RelationalPlaceholder<?> that)) return false;
    return that.key.equalsIgnoreCase(this.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return "RelationalPlaceholder{" +
            "key='" + key + '\'' +
            '}';
  }
}