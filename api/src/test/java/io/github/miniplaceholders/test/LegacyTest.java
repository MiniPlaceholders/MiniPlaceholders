package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.utils.LegacyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyTest {
    @Test
    void testLegacyParsing() {
        final String string = "<rainbow>hello... i &8hate legacy format §bso much";
        final Component parsed = LegacyUtils.parsePossibleLegacy(string);
        final String result = PlainTextComponentSerializer.plainText().serialize(parsed);
        final String expected = "hello... i hate legacy format so much";

        assertEquals(expected, result);
    }
}
