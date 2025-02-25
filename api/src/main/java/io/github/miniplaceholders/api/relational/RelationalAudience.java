package io.github.miniplaceholders.api.relational;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;

public record RelationalAudience<A extends Audience>(A audience, A otherAudience) implements ForwardingAudience.Single {
  public static <A extends Audience>  RelationalAudience<A> from(A audience, A relational) {
    return new RelationalAudience<>(audience, relational);
  }
}
