package io.github.miniplaceholders.test.loader;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.types.Platform;

public class TestExpansionProvider implements ExpansionProvider {
  @Override
  public Expansion provideExpansion() {
    return Expansion.builder("provided").build();
  }

  @Override
  public LoadRequirement loadRequirement() {
    return LoadRequirement.allOf(
            LoadRequirement.platform(Platform.FABRIC, Platform.PAPER),
            LoadRequirement.requiredComplement("luckperms", "LuckPerms")
    );
  }
}
