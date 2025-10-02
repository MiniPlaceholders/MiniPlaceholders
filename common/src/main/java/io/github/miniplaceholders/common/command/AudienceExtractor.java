package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface AudienceExtractor<S> {
  Audience extract(S source);
}
