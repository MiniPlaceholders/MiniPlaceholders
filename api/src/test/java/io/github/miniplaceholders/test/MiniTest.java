package io.github.miniplaceholders.test;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface MiniTest {
  default void assertContentEquals(Component first, Component second) {
    String firstSerialized = PlainTextComponentSerializer.plainText().serialize(first);
    String secondSerialized = PlainTextComponentSerializer.plainText().serialize(second);

    assertEquals(firstSerialized, secondSerialized);
  }

  default void assertSimilarity(String string, Component component) {
    final String serialized = PlainTextComponentSerializer.plainText().serializeOrNull(component);

    assertNotNull(serialized);

    assertEquals(string, serialized);
  }

  default Component parse(String string, TagResolver resolver) {
    return MiniMessage.miniMessage().deserialize(string, resolver);
  }
}
