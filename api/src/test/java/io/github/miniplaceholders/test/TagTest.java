package io.github.miniplaceholders.test;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.ParserDirective;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTest implements MiniTest {
  @Test
  void testTag() {
    Expansion.builder("tags")
            .globalPlaceholder("modifying", FilteringTag::new)
            .globalPlaceholder("parser_directive", ParserDirective.RESET)
            .build()
            .register();

    final TagResolver globalResolver = MiniPlaceholders.globalPlaceholders();
    assertSimilarity("------ replacement ------", parse("------ <tags_modifying>test</tags_modifying> ------", globalResolver));
    assertEquals(Component.text()
                    .append(Component.text("aaaaaa", NamedTextColor.RED), Component.text("aaaaaaa"))
                    .build(),
                parse("<red>aaaaaa<tags_parser_directive>aaaaaaa", globalResolver));
  }

  public record FilteringTag(ArgumentQueue queue, Context context) implements Modifying {
    @Override
    public Component apply(@NotNull Component current, int depth) {
      if (depth != 0) {
        return Component.empty();
      }
      final String serialized = PlainTextComponentSerializer.plainText().serialize(current);
      if (serialized.contains("test")) {
        return Component.text(serialized.replace("test", "replacement"));
      }
      return current;
    }
  }
}
