package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.common.command.PlayersNameProvider;

import java.util.List;

public interface PlayerCompleterNode {
  default List<String> providePlayerSuggestions() {
    final List<String> suggestions = playersNameProvider().provide();
    suggestions.add("me");
    return suggestions;
  }

  PlayersNameProvider playersNameProvider();
}
