package io.github.miniplaceholders.common.command;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

@FunctionalInterface
public interface PlayersNameProvider {
  @NonNull ArrayList<String> provide();
}
