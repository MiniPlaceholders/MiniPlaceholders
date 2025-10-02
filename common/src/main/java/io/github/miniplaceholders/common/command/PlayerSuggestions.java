package io.github.miniplaceholders.common.command;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

@FunctionalInterface
public interface PlayerSuggestions {
  @NonNull ArrayList<String> suggest();
}
