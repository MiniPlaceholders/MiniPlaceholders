package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.utils.LegacyStrings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.PreProcess;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyTest implements MiniTest {
    @Test
    void testLegacyParsing() {
        final String string = "<rainbow>hello... i &8hate legacy format §bso much";
        final Component parsed = LegacyStrings.parsePossibleLegacy(string);
        final String result = PlainTextComponentSerializer.plainText().serialize(parsed);
        final String expected = "hello... i hate legacy format so much";

        assertEquals(expected, result);
    }

    @Test
    void testMapLegacyOrElse() {
        final String legacyInput = "&fhi <red>aaaa";
        Tag legacyProcessedTag = LegacyStrings.mapLegacyOrElse(
                legacyInput,
                legacy -> Tag.inserting(LegacyStrings.parsePossibleLegacy(legacy)),
                Tag::preProcessParsed
        );
        assertInstanceOf(Inserting.class, legacyProcessedTag);

        final String modernInput = "<rainbow>aaaaaaaaaaaaa";
        Tag modernProcessedTag = LegacyStrings.mapLegacyOrElse(
                modernInput,
                __ -> fail(),
                Tag::preProcessParsed
        );
        assertInstanceOf(PreProcess.class, modernProcessedTag);
    }
}
