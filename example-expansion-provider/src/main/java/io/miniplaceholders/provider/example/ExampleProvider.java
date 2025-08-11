package io.miniplaceholders.provider.example;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.provider.example.ExpansionConstants;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class ExampleProvider implements ExpansionProvider {
  @Override
  public Expansion provideExpansion() {
    return Expansion.builder("example")
            .globalPlaceholder("test",
                    (queue, context) -> Tag.preProcessParsed("Provided"))
            .version(ExpansionConstants.VERSION)
            .build();
  }

  @Override
  public LoadRequirement loadRequirement() {
    return LoadRequirement.allOf(LoadRequirement.allOf(LoadRequirement.platform(Platform.VELOCITY)));
  }
}
