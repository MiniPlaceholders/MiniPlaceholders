package io.github.miniplaceholders.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void registrationTest() {
        Expansion expansion = Expansion.builder("testregistration")
                .build();
        expansion.register();

        assertTrue(expansion.registered());
        assertThrows(IllegalStateException.class, expansion::register);

        assertDoesNotThrow(expansion::unregister);
    }
}
