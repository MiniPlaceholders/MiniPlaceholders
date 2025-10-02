package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.common.command.PlayerSuggestions;

import java.util.List;

public interface PlayerSuggestionProvider {
  default List<String> providePlayerSuggestions() {
    final List<String> suggestions = playerSuggestions().suggest();
    suggestions.add("me");
    return suggestions;
  }

  PlayerSuggestions playerSuggestions();
}
