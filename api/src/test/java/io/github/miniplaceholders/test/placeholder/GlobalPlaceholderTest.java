package io.github.miniplaceholders.test.placeholder;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.test.MiniTest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GlobalPlaceholderTest implements MiniTest {
  @Test
  @DisplayName("Global Placeholder Test")
  void globalExpansionPlaceholderTest() {
    Expansion expansion = Expansion.builder("global")
        .globalPlaceholder("players", (queue, ctx) -> Tag.selfClosingInserting(Component.text(1305)))
        .globalPlaceholder("servers", (queue, ctx) -> Tag.selfClosingInserting(Component.text(7)))
        .build();

    final Component expected = Component.text("Online players: 1305 | Servers: 7");
    final Component actual = MiniMessage.miniMessage().deserialize("Online players: <global_players> | Servers: <global_servers>",
        expansion.globalPlaceholders());

    assertContentEquals(expected, actual);
  }
}
