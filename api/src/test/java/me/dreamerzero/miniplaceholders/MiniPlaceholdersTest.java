package me.dreamerzero.miniplaceholders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniPlaceholdersTest {
    @Test
    @Disabled("The tagresolvers implemented classes does not implement the equals method")
    void methodEquality(){
        Expansion.builder("equality")
            .audiencePlaceholder("audience", TagsUtils.NULL_AUDIENCE_PLACEHOLDER)
            .globalPlaceholder("global", TagsUtils.EMPTY_TAG)
            .build()
        .register();


        assertEquals(
            MiniPlaceholders.getAudienceGlobalPlaceholders(Audience.empty()),
            TagResolver.resolver(
                MiniPlaceholders.getAudiencePlaceholders(Audience.empty()),
                MiniPlaceholders.getGlobalPlaceholders()
            )
        );
    }
}
