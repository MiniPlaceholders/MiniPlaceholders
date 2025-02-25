package io.github.miniplaceholders.api.relational;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.pointer.Pointered;

public record RelationalAudience<A extends Audience>(A audience, A otherAudience) implements Pointered {
  public static <A extends Audience>  RelationalAudience<A> from(A audience, A relational) {
    return new RelationalAudience<>(audience, relational);
  }
}
