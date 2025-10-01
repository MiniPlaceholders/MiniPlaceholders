package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentTest implements MiniTest {
  @Test
  @DisplayName("Empty Arguments")
  void testEmptyArguments() {
    final Expansion expansion = Expansion.builder("test")
        .globalPlaceholder("testing", (queue, ctx) -> {
          int arguments = 0;
          while (queue.hasNext()) {
            arguments++;
            Tag.Argument argument = queue.pop();
            assertTrue(argument.value().isBlank());
          }
          return Tag.selfClosingInserting(Component.text("Arguments: " + arguments));
        })
        .build();

    Component parsed = miniMessage().deserialize("<test_testing::>", expansion.globalPlaceholders());

    assertContentEquals(parsed, Component.text("Arguments: 2"));
  }
}
