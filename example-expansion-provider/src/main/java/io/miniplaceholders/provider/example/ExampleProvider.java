package io.miniplaceholders.provider.example;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.provider.example.ExpansionConstants;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class ExampleProvider implements ExpansionProvider {
    //    For advanced usage only
    @team.unnamed.inject.Inject
    private io.github.miniplaceholders.api.provider.PlatformData platformData;

    @Override
    public Expansion provideExpansion() {

        return Expansion.builder("example")
                .version("2.0.0")
                .author("MiniPlaceholders Contributors")
                .globalPlaceholder("test", (queue, context) ->
                        Tag.preProcessParsed("""
                                Platform Instance Class: %s
                                MiniPlaceholders Instance Class: %s
                                """.formatted(
                                platformData.serverInstance(),
                                platformData.complementInstance())))
                .version(ExpansionConstants.VERSION)
                .build();
    }

    @Override
    public LoadRequirement loadRequirement() {
        return LoadRequirement.allOf(LoadRequirement.allOf(LoadRequirement.platform(Platform.VELOCITY)));
    }
}
