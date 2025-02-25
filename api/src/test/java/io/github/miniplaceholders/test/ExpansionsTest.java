package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.placeholder.GlobalPlaceholder;
import io.github.miniplaceholders.api.types.PlaceholderType;
import io.github.miniplaceholders.api.utils.Tags;
import io.github.miniplaceholders.test.instances.TestAudience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpansionsTest implements MiniTest {
    @Test
    @DisplayName("Expansion Equality")
    void equalsExpansions(){
        final Expansion firstExpansion = Expansion.builder("equals")
            .audiencePlaceholder("test", Tags.emptyAudienceResolver())
            .build();
        final Expansion secondExpansion = Expansion.builder("equals")
            .audiencePlaceholder("test", Tags.emptyAudienceResolver())
            .build();

        assertEquals(firstExpansion, secondExpansion);
    }

    @Test
    void placeholderRetrievalTest() {
        final Expansion expansion = Expansion.builder("test")
                .globalPlaceholder("some_placeholder", Tag.preProcessParsed("test"))
                .build();

        assertTrue(expansion.hasGlobalPlaceholder("test_some_placeholder"));
        final GlobalPlaceholder somePlaceholder = expansion.globalPlaceholderByName("test_some_placeholder");
        assertNotNull(somePlaceholder);

        final var expansionResult = parse("Result: <test_some_placeholder>", expansion.globalPlaceholders());
        final var placeholderResult = parse("Result: <test_some_placeholder>", somePlaceholder);
        assertSimilarity("Result: test", expansionResult);
        assertSimilarity("Result: test", placeholderResult);
    }

    @Test
    void placeholdersByType() {
        final Expansion expansion = Expansion.builder("idk")
                .audiencePlaceholder(TestAudience.class, "test", Tags.emptyAudienceResolver())
                .build();

        var placeholders = expansion.placeholdersByType(PlaceholderType.AUDIENCE);
        assertNotNull(placeholders);
        assertEquals(placeholders, expansion.audiencePlaceholders());
    }
}
