package io.github.miniplaceholders.common.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@FunctionalInterface
public interface PlayerSuggestions {
  @NotNull ArrayList<String> suggest();
}
