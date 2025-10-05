package io.miniplaceholders.provider.example;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.provider.PlatformData;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.provider.example.ExpansionConstants;
import net.kyori.adventure.text.minimessage.tag.Tag;
import team.unnamed.inject.Inject;

public record ExampleProvider(PlatformData platformData) implements ExpansionProvider {
  @Inject
  public ExampleProvider {}

  @Override
  public Expansion provideExpansion() {
    return Expansion.builder("example")
        .author("MiniPlaceholders Contributors")
        .globalPlaceholder("test", (queue, context) ->
            Tag.preProcessParsed("""
                Platform Instance Class: %s
                MiniPlaceholders Instance Class: %s
                """.formatted(
                platformData.serverInstance(),
                platformData.complementInstance()
            )))
        .version(ExpansionConstants.VERSION)
        .build();
  }

  @Override
  public LoadRequirement loadRequirement() {
    return LoadRequirement.anyOf(
        LoadRequirement.platform(Platform.SPONGE),
        LoadRequirement.allOf(
            LoadRequirement.requiredComplement(
                "MiniPlaceholders",
                "miniplaceholders"
            ),
            LoadRequirement.platform(Platform.PAPER)
        ),
        LoadRequirement.requiredClass("io.github.miniplaceholders.common.command.node.PlayerCompleterNode")
    );
  }
}
