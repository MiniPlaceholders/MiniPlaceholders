package io.github.miniplaceholders.test;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.junit.jupiter.api.Assertions.*;

import io.github.miniplaceholders.test.testobjects.TestAudience;
import io.github.miniplaceholders.test.testobjects.TestAudienceHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

class PlaceholderTest {

    @Test
    @DisplayName("Audience Placeholder Test")
    void audienceExpansionPlaceholdersTest(){
        TestAudience player = new TestAudience("4drian3d");

        Expansion expansion = Expansion.builder("example")
            .audiencePlaceholder("name", (aud, queue, ctx) -> {
                TestAudience audience = (TestAudience) aud;
                return Tag.selfClosingInserting(Component.text(audience.name()));
            }).build();

        final Component expected = Component.text("Player Name: 4drian3d");
        final Component result = miniMessage().deserialize("Player Name: <example_name>", expansion.audiencePlaceholders(player));

        assertContentEquals(expected, result);
    }

    @Test
    @DisplayName("Relational Placeholder Test")
    void relationalExpansionPlaceholdersTest(){
        Audience p1 = new TestAudience("PlayerOne");
        Audience p2 = new TestAudience("PlayerTwo");

        Expansion expansion = Expansion.builder("relational")
            .relationalPlaceholder("enemy",
                (aud, otheraud, queue, ctx) -> Tag.selfClosingInserting(this.isEnemy(((TestAudience)aud),((TestAudience)otheraud))
                    ? Component.text("Enemy", NamedTextColor.RED)
                    : Component.text("Neutral", NamedTextColor.GREEN)
                ))
            .build();

        assertEquals(MiniMessage.miniMessage().deserialize("You are <red>Enemy"), MiniMessage.miniMessage().deserialize("You are <relational_rel_enemy>", expansion.relationalPlaceholders(p1, p2)));
    }

    @Test
    @DisplayName("Global Placeholder Test")
    void globalExpansionPlaceholderTest(){
        Expansion expansion = Expansion.builder("global")
            .globalPlaceholder("players", (queue, ctx) -> Tag.selfClosingInserting(Component.text(1305)))
            .globalPlaceholder("servers", (queue, ctx) -> Tag.selfClosingInserting(Component.text(7)))
            .build();

        final Component expected = Component.text("Online players: 1305 | Servers: 7");
        final Component actual = MiniMessage.miniMessage().deserialize("Online players: <global_players> | Servers: <global_servers>", expansion.globalPlaceholders());

        assertContentEquals(expected, actual);
    }

    @Test
    @DisplayName("Registered Placeholders")
    void instancePlaceholdersTest(){
        Expansion.builder("instance")
            .globalPlaceholder("hello", (queue, ctx) -> Tag.selfClosingInserting(Component.text("hello", NamedTextColor.RED)))
            .build()
            .register();

        Component expected = Component.text("hello player", NamedTextColor.RED);
        Component actual = MiniMessage.miniMessage().deserialize("<instance_hello> player", MiniPlaceholders.getGlobalPlaceholders());

        assertContentEquals(expected, actual);
    }

    @Test
    @DisplayName("Filtered Expansion")
    void filteredExpansion(){
        Expansion expansion = Expansion.builder("filter")
            .audiencePlaceholder("name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((TestAudience)aud).name())))
            .filter(TestAudience.class)
            .build();

        Audience player = new TestAudience("TestPlayer04");
        Audience emptyAudience = Audience.empty();

        String string = "Player Name: <filter_name>";

        Component playerExpected = Component.text("Player Name: TestPlayer04");
        Component emptyExpected = Component.text("Player Name: <filter_name>");

        assertContentEquals(playerExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(player)));
        assertContentEquals(emptyExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(emptyAudience)));
    }

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
                    return Tag.selfClosingInserting(Component.text("Arguments: "+arguments));
                })
                .build();

        Component parsed = miniMessage().deserialize("<test_testing::>", expansion.globalPlaceholders());

        assertContentEquals(parsed, Component.text("Arguments: 2"));
    }

    @Test
    @DisplayName("Forwarding Audience Parsing Test")
    void forwardingAudienceTest() {
        Expansion expansion = Expansion.builder("test")
                .filter(TestAudience.class)
                .audiencePlaceholder("testing", (audience, queue, ctx) -> {
                    TestAudience testAudience = (TestAudience) audience;
                    return Tag.selfClosingInserting(Component.text("Name: "+testAudience.name()));
                }).build();

        TestAudience testAudience = new TestAudience("4drian3d");
        Audience forward = new TestAudienceHolder(testAudience);

        Component parsed = miniMessage().deserialize("<test_testing>.", expansion.audiencePlaceholders(forward));
        Component expected = Component.text("Name: 4drian3d.");

        assertContentEquals(parsed, expected);
    }

    static void assertContentEquals(Component first, Component second){
        String firstSerialized = PlainTextComponentSerializer.plainText().serialize(first);
        String secondSerialized = PlainTextComponentSerializer.plainText().serialize(second);

        assertEquals(firstSerialized, secondSerialized);
    }

    private boolean isEnemy(TestAudience a1, TestAudience a2){
        return a1.name().equals("PlayerOne") && a2.name().equals("PlayerTwo");
    }

}
