package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.TagsUtils;
import io.github.miniplaceholders.test.testobjects.TestAudienceHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.miniplaceholders.test.PlaceholderTest.assertContentEquals;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpansionsTest {
    @Test
    @DisplayName("Expansion Equality")
    void equalsExpansions(){
        final Expansion firstExpansion = Expansion.builder("equals")
            .audiencePlaceholder("test", TagsUtils.NULL_AUDIENCE_PLACEHOLDER)
            .build();
        final Expansion secondExpansion = Expansion.builder("equals")
            .audiencePlaceholder("test", TagsUtils.NULL_AUDIENCE_PLACEHOLDER)
            .build();

        assertEquals(firstExpansion, secondExpansion);
    }

    @Test
    void nullForwardedAudience() {
        TestAudienceHolder audience = new TestAudienceHolder(null);

        final Expansion expansion = Expansion.builder("test")
                .filter(aud -> aud.toString().isBlank())
                .audiencePlaceholder("testing", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(aud.toString())))
                .build();

        Component result = assertDoesNotThrow(
                () -> miniMessage().deserialize("<test_testing>", expansion.audiencePlaceholders(audience))
        );

        assertContentEquals(result, Component.text("<test_testing>"));
    }
}
