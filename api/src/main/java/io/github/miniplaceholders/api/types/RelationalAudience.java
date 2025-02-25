package io.github.miniplaceholders.api.types;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jspecify.annotations.NullMarked;

/**
 * A relationship between 2 Audiences.
 *
 * @param audience the principal audience
 * @param relational the secondary audience, with which the relationship will be evaluated
 * @param <A> the audiences type
 * @since 3.0.0
 */
@NullMarked
public record RelationalAudience<A extends Audience>(A audience, A relational) implements ForwardingAudience.Single {
  /**
   * Creates a new relation between two audiences.
   *
   * @param audience the principal audience
   * @param relational the secondary audience, with which the relationship will be evaluated
   * @return a new relation
   * @param <A> the audiences type
   * @since 3.0.0
   */
  public static <A extends Audience>  RelationalAudience<A> from(A audience, A relational) {
    return new RelationalAudience<>(audience, relational);
  }
}
