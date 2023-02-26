package io.github.miniplaceholders.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

class MiniPlaceholdersTest {
    @Test
    @Disabled("Cannot correctly calculate the equity of 2 TagResolvers obtained in different ways")
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
