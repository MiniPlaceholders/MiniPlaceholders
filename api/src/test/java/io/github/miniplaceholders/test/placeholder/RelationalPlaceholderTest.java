package io.github.miniplaceholders.test.placeholder;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.placeholder.PlaceholderMetadata;
import io.github.miniplaceholders.api.types.RelationalAudience;
import io.github.miniplaceholders.test.MiniTest;
import io.github.miniplaceholders.test.instances.TestAudience;
import io.github.miniplaceholders.test.instances.TestAudienceHolder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelationalPlaceholderTest implements MiniTest {
  @Test
  @DisplayName("Relational Placeholder Test")
  void relationalExpansionPlaceholdersTest() {
    Audience p1 = new TestAudience("PlayerOne");
    Audience p2 = new TestAudience("PlayerTwo");

    Expansion expansion = Expansion.builder("relational")
        .relationalPlaceholder(TestAudience.class, "enemy",
            (aud, relational, queue, ctx) ->
                Tag.selfClosingInserting(this.isEnemy(aud, relational)
                    ? Component.text("Enemy", NamedTextColor.RED)
                    : Component.text("Neutral", NamedTextColor.GREEN)
                ))
        .build();

    assertEquals(
        MiniMessage.miniMessage().deserialize("You are <red>Enemy"),
        MiniMessage.miniMessage().deserialize("You are <relational_rel_enemy>",
            RelationalAudience.from(p1, p2),
            expansion.relationalPlaceholders()));
  }

  private boolean isEnemy(TestAudience a1, TestAudience a2) {
    return a1.name().equals("PlayerOne") && a2.name().equals("PlayerTwo");
  }

  @Test
  @DisplayName("Relational Forwarding Audience Parsing Test")
  void relationalForwardingAudienceTest() {
    Expansion expansion = Expansion.builder("test")
        .relationalPlaceholder(TestAudience.class, "testing",
            (audience, another, queue, ctx) ->
                Tag.selfClosingInserting(Component.text("Name: " + audience.name() + ", Other Name: " + another.name())))
        .build();

    TestAudience testAudience = new TestAudience("4drian3d");
    TestAudience secondAudience = new TestAudience("PepitoAlcachofa");
    Audience forward = new TestAudienceHolder(testAudience);
    Audience secondForward = new TestAudienceHolder(secondAudience);

    Component parsed = miniMessage().deserialize("<test_rel_testing>.",
        RelationalAudience.from(forward, secondForward), expansion.relationalPlaceholders());
    Component expected = Component.text("Name: 4drian3d, Other Name: PepitoAlcachofa.");

    assertContentEquals(expected, parsed);
  }

  @Test
  @DisplayName("Relational Filtered Expansion")
  void relationalFilteredExpansion() {
    Expansion expansion = Expansion.builder("filter")
        .relationalPlaceholder(TestAudience.class, "name",
            (aud, another, queue, ctx) ->
                Tag.selfClosingInserting(Component.text(aud.name().replace(another.name(), ""))))
        .build();

    Audience player = new TestAudience("TestPlayer04");
    Audience second = new TestAudience("Player");
    Audience emptyAudience = Audience.empty();

    String string = "Player Name: <filter_rel_name>";

    Component playerExpected = Component.text("Player Name: Test04");
    Component emptyExpected = Component.text("Player Name: <filter_rel_name>");

    assertContentEquals(playerExpected, MiniMessage.miniMessage().deserialize(string,
        RelationalAudience.from(player, second), expansion.relationalPlaceholders()));
    assertContentEquals(emptyExpected, MiniMessage.miniMessage().deserialize(string,
        RelationalAudience.from(player, emptyAudience), expansion.relationalPlaceholders()));
  }

  @Test
  @DisplayName("RelationalPlaceholder Builder creation")
  void builderRelationalPlaceholderCreation() {
    final Expansion expansion = Expansion.builder("builder")
        .relationalPlaceholder(
            TestAudience.class,
            builder -> builder.name("test")
                .resolver((audience, relational, queue, ctx) -> {
                  final String name = audience.name();
                  final String relationalName = relational.name();
                  return Tag.preProcessParsed(name + " and " + relationalName);
                })
                .metadata(PlaceholderMetadata.data("Builder Test", "Returns the Audience plus the Relational Audience combined Names"))
                .targetFilter(target -> target.name().length() <= 16)
        ).build();
    final TestAudience audience = new TestAudience("4drian3d");
    final TestAudience relational = new TestAudience("PepitoAlcachofa");
    final RelationalAudience<@NotNull TestAudience> relationalAudience = RelationalAudience.from(audience, relational);

    Component expected = Component.text("Our names are: 4drian3d and PepitoAlcachofa");
    Component parsed = miniMessage().deserialize("Our names are: <builder_test>", relationalAudience, expansion.relationalPlaceholders());

    assertContentEquals(expected, parsed);
  }
}
