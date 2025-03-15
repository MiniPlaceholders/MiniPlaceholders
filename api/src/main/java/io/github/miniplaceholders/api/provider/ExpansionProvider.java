package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.Expansion;

public interface ExpansionProvider {
  Expansion provideExpansion();

  LoadRequirement loadRequirement();
}
