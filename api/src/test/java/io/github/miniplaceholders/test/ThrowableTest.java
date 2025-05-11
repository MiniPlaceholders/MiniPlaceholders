package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.utils.Tags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

//yea... it is intended xd
@SuppressWarnings("DataFlowIssue")
class ThrowableTest implements MiniTest {
    @Test
    @DisplayName("Nullable Exception")
    void throwOnNull(){
        assertThrows(NullPointerException.class, () -> Expansion.builder(null));

        Expansion.Builder expansionTest = assertDoesNotThrow(() -> Expansion.builder("throwable"));

        assertThrows(NullPointerException.class, () -> expansionTest.audiencePlaceholder(null, (aud, queue, ctx) -> Tags.EMPTY_TAG));
        assertThrows(NullPointerException.class, () -> expansionTest.globalPlaceholder("test", (GlobalTagResolver) null));
    }

    @Test
    @DisplayName("Empty Exception")
    void throwOnBlankString(){
        Expansion.Builder builder = assertDoesNotThrow(() -> Expansion.builder("hello"));

        assertThrows(IllegalStateException.class, () -> builder.audiencePlaceholder("", (aud, queue, ctx) -> Tags.EMPTY_TAG));
        assertThrows(IllegalStateException.class, () -> builder.globalPlaceholder("     ", (queue, ctx) -> Tags.EMPTY_TAG));

        assertDoesNotThrow(() -> builder.globalPlaceholder("hehe", (queue, ctx) -> Tags.EMPTY_TAG));
    }
}
