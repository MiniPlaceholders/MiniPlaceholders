package me.dreamerzero.miniplaceholders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.testobjects.TestAudience;
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
            .audiencePlaceholder("name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((TestAudience)aud).getName()))).build();

        final Component expected = Component.text("Player Name: 4drian3d");
        final Component result = MiniMessage.miniMessage().deserialize("Player Name: <example_name>", expansion.audiencePlaceholders(player));

        assertEquals(expected, result);
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

        assertEquals(expected, actual);
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
            .audiencePlaceholder("name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((TestAudience)aud).getName())))
            .filter(TestAudience.class)
            .build();

        Audience player = new TestAudience("TestPlayer04");
        Audience emptyAudience = Audience.empty();

        String string = "Player Name: <filter_name>";

        Component playerExpected = Component.text("Player Name: TestPlayer04");
        Component emptyExpected = Component.text("Player Name: <filter_name>");

        assertEquals(playerExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(player)));
        assertEquals(emptyExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(emptyAudience)));


    }

    void assertContentEquals(Component first, Component second){
        String firstSerialized = PlainTextComponentSerializer.plainText().serialize(first);
        String secondSerialized = PlainTextComponentSerializer.plainText().serialize(second);

        assertEquals(firstSerialized, secondSerialized);
    }

    private boolean isEnemy(TestAudience a1, TestAudience a2){
        return a1.getName().equals("PlayerOne") && a2.getName().equals("PlayerTwo");
    }

}
