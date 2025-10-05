package io.github.miniplaceholders.common.command.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.miniplaceholders.common.command.node.PlayerCompleterNode;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public record BrigadierSuggestionProvider<A>(PlayerCompleterNode playerSuggestions) implements SuggestionProvider<A> {
  @Override
  public CompletableFuture<Suggestions> getSuggestions(final CommandContext<A> context, final SuggestionsBuilder builder) {
    this.playerSuggestions.providePlayerSuggestions().forEach(builder::suggest);
    return builder.buildFuture();
  }
}
