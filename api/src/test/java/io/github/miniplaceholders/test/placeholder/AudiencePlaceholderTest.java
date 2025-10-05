package io.github.miniplaceholders.test.placeholder;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.placeholder.PlaceholderMetadata;
import io.github.miniplaceholders.test.MiniTest;
import io.github.miniplaceholders.test.instances.TestAudience;
import io.github.miniplaceholders.test.instances.TestAudienceHolder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class AudiencePlaceholderTest implements MiniTest {
  @Test
  @DisplayName("Audience Placeholder Test")
  void audienceExpansionPlaceholdersTest() {
    TestAudience player = new TestAudience("4drian3d");

    Expansion expansion = Expansion.builder("example")
        .audiencePlaceholder("name", (aud, queue, ctx) -> {
          TestAudience audience = (TestAudience) aud;
          return Tag.selfClosingInserting(Component.text(audience.name()));
        }).build();

    final Component expected = Component.text("Player Name: 4drian3d");
    final Component result = miniMessage().deserialize("Player Name: <example_name>", player, expansion.audiencePlaceholders());

    assertContentEquals(expected, result);
  }

  @Test
  @DisplayName("Forwarding Audience Parsing Test")
  void forwardingAudienceTest() {
    Expansion expansion = Expansion.builder("test")
        .audiencePlaceholder(TestAudience.class, "testing",
            (audience, queue, ctx) -> Tag.selfClosingInserting(Component.text("Name: " + audience.name())))
        .build();

    TestAudience testAudience = new TestAudience("4drian3d");
    Audience forward = new TestAudienceHolder(testAudience);

    Component parsed = miniMessage().deserialize("<test_testing>.", forward, expansion.audiencePlaceholders());
    Component expected = Component.text("Name: 4drian3d.");

    assertContentEquals(parsed, expected);
  }

  @Test
  @DisplayName("Filtered Expansion")
  void filteredExpansion() {
    Expansion expansion = Expansion.builder("filter")
        .audiencePlaceholder(TestAudience.class, "name",
            (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(aud.name())))
        .build();

    Audience player = new TestAudience("TestPlayer04");
    Audience emptyAudience = Audience.empty();

    String string = "Player Name: <filter_name>";

    Component playerExpected = Component.text("Player Name: TestPlayer04");
    Component emptyExpected = Component.text("Player Name: <filter_name>");

    assertContentEquals(playerExpected, MiniMessage.miniMessage().deserialize(string, player, expansion.audiencePlaceholders()));
    assertContentEquals(emptyExpected, MiniMessage.miniMessage().deserialize(string, emptyAudience, expansion.audiencePlaceholders()));
  }

  @Test
  @DisplayName("AudiencePlaceholder Builder creation")
  void builderAudiencePlaceholderCreation() {
    final Expansion expansion = Expansion.builder("builder")
        .audiencePlaceholder(
            TestAudience.class,
            builder -> builder.name("test")
                .resolver((audience, queue, ctx) -> {
                  final String name = audience.name();
                  return Tag.preProcessParsed(name);
                })
                .metadata(PlaceholderMetadata.data("Builder Test", "Returns the Audience Name"))
                .targetFilter(target -> target.name().length() <= 16)
        ).build();
    final TestAudience audience = new TestAudience("4drian3d");

    Component expected = Component.text("My name is: 4drian3d");
    Component parsed = miniMessage().deserialize("My name is: <builder_test>", audience, expansion.audiencePlaceholders());

    assertContentEquals(expected, parsed);
  }
}
