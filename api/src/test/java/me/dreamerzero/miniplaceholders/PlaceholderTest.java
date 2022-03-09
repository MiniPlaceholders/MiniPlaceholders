package me.dreamerzero.miniplaceholders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.GlobalPlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;

public class PlaceholderTest {

    /*@Test
    @DisplayName("Audience Placeholder Test")
    void audienceExpansionPlaceholdersTest(){
        Player player = new TestPlayer("4drian3d");

        Expansion expansion = Expansion.builder("example")
            .audiencePlaceholder(AudiencePlaceholder.create(
                "name", p -> Tag.selfClosingInserting(Component.text(((Player)p).getUsername()))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "uuid", p -> Tag.selfClosingInserting(Component.text(((Player)p).getUniqueId().toString()))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablistheader", p-> Tag.selfClosingInserting(((Player)p).getPlayerListHeader())
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablistfooter", p -> Tag.selfClosingInserting(((Player)p).getPlayerListFooter())
            ))
            .build();

        final Component expected = Component.text("Player Name: 4drian3d");
        final Component result = MiniMessage.miniMessage().deserialize("Player Name: <example_name>", expansion.audiencePlaceholders(player));

        assertEquals(expected, result);
    }

    /*@Test
    @DisplayName("Relational Placeholder Test")
    void relationalExpansionPlaceholdersTest(){
        /*Player p1 = new TestPlayer("PlayerOne");
        Player p2 = new TestPlayer("PlayerTwo");

        Expansion expansion = Expansion.builder("relational")
            .relationalPlaceholder(RelationalPlaceholder.create(
                "enemy", (p, o) -> Tag.selfClosingInserting(this.isEnemy(p,o) ? Component.text("Enemy", NamedTextColor.RED) : Component.text("Neutral", NamedTextColor.GREEN))))
            .build();

        assertEquals(MiniMessage.miniMessage().deserialize("You are <red>Enemy"), MiniMessage.miniMessage().deserialize("You are <relational_rel_enemy>", expansion.relationalPlaceholders(p1, p2)));
    }

    @Test
    @DisplayName("Global Placeholder Test")
    void globalExpansionPlaceholderTest(){
        ProxyServer proxy = mock(ProxyServer.class);
        when(proxy.getAllPlayers()).thenReturn(Set.of());
        when(proxy.getAllServers()).thenReturn(Set.of());
        when(proxy.getPlayer("Juan")).thenReturn(Optional.empty());

        Expansion expansion = Expansion.builder("global")
            .globalPlaceholder(GlobalPlaceholder.create("players", Tag.selfClosingInserting(Component.text(proxy.getAllPlayers().size()))))
            .globalPlaceholder(GlobalPlaceholder.create("servers", Tag.selfClosingInserting(Component.text(proxy.getAllServers().size()))))
            .build();

        final Component expected = Component.text("Online players: 0 | Servers: 0");
        final Component actual = MiniMessage.miniMessage().deserialize("Online players: <global_players> | Servers: <global_servers>", expansion.globalPlaceholders());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registered Placeholders")
    void instancePlaceholdersTest(){
        Expansion.builder("instance")
            .globalPlaceholder(GlobalPlaceholder.create("hello", Tag.selfClosingInserting(Component.text("hello", NamedTextColor.RED))))
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
            .audiencePlaceholder(AudiencePlaceholder.create("name", (aud) -> Tag.selfClosingInserting(Component.text(((Player)aud).getUsername()))))
            .filter(Audience.class)
            .build();

        Player player = new TestPlayer("TestPlayer04");
        Audience emptyAudience = Audience.empty();

        String string = "Player Name: <filter_name>";

        Component playerExpected = Component.text("Player Name: TestPlayer04");
        Component emptyExpected = Component.text("Player Name: <filter_name>");

        assertEquals(playerExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(player)));
        assertEquals(emptyExpected, MiniMessage.miniMessage().deserialize(string, expansion.audiencePlaceholders(emptyAudience)));


    }

    private void assertContentEquals(Component first, Component second){
        String firstSerialized = PlainTextComponentSerializer.plainText().serialize(first);
        String secondSerialized = PlainTextComponentSerializer.plainText().serialize(second);

        assertEquals(firstSerialized, secondSerialized);
    }*/

}