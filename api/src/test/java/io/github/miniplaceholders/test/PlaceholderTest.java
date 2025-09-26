package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlaceholderTest implements MiniTest {

  @Test
  @DisplayName("Registered Placeholders")
  void instancePlaceholdersTest() {
    Expansion.builder("instance")
            .globalPlaceholder("hello", (queue, ctx) ->
                Tag.selfClosingInserting(Component.text("hello", NamedTextColor.RED)))
            .build()
            .register();

    Component expected = Component.text("hello player", NamedTextColor.RED);
    Component actual = MiniMessage.miniMessage().deserialize("<instance_hello> player", MiniPlaceholders.globalPlaceholders());

    assertContentEquals(expected, actual);
  }

}
