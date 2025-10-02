package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;

@FunctionalInterface
public interface AudienceExtractor<S> {
  Audience extract(S source);
}
