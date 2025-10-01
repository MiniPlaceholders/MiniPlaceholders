package io.github.miniplaceholders.api.placeholder;

import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

@NullMarked
public record PlaceholderMetadata(String displayName, String description) {
  public PlaceholderMetadata {
    requireNonNull(displayName);
    requireNonNull(description);
  }

  public static PlaceholderMetadata data(final String displayName, final String description) {
    return new PlaceholderMetadata(displayName, description);
  }
}
