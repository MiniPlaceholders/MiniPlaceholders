package io.github.miniplaceholders.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.TagsUtils;

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
}
