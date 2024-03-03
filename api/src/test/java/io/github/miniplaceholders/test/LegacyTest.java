package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.utils.LegacyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.PreProcess;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyTest {
    @Test
    void testLegacyParsing() {
        final String string = "<rainbow>hello... i &8hate legacy format §bso much";
        final Component parsed = LegacyUtils.parsePossibleLegacy(string);
        final String result = PlainTextComponentSerializer.plainText().serialize(parsed);
        final String expected = "hello... i hate legacy format so much";

        assertEquals(expected, result);
    }

    @Test
    void testMapLegacyOrElse() {
        final String legacyInput = "&fhi <red>aaaa";
        Tag legacyProcessedTag = LegacyUtils.mapLegacyOrElse(
                legacyInput,
                legacy -> Tag.inserting(LegacyUtils.parsePossibleLegacy(legacy)),
                Tag::preProcessParsed
        );
        assertInstanceOf(Inserting.class, legacyProcessedTag);

        final String modernInput = "<rainbow>aaaaaaaaaaaaa";
        Tag modernProcessedTag = LegacyUtils.mapLegacyOrElse(
                modernInput,
                __ -> fail(),
                Tag::preProcessParsed
        );
        assertInstanceOf(PreProcess.class, modernProcessedTag);
    }
}
