package me.dreamerzero.miniplaceholders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import me.dreamerzero.miniplaceholders.testobjects.TestPlayer;
import me.dreamerzero.miniplaceholders.velocity.Expansion;
import me.dreamerzero.miniplaceholders.velocity.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.velocity.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.RelationalPlaceholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class PlaceholderTest {

    @Test
    @DisplayName("Audience Placeholder Test")
    void audienceExpansionPlaceholdersTest(){
        Player player = new TestPlayer("4drian3d");

        Expansion expansion = Expansion.builder("example")
            .audiencePlaceholder(AudiencePlaceholder.create(
                "name", p -> Component.text(((Player)p).getUsername())
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "uuid", p -> Component.text(((Player)p).getUniqueId().toString())
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablistheader", p-> ((Player)p).getPlayerListHeader()
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablistfooter", p -> ((Player)p).getPlayerListFooter()
            ))
            .build();

        final Component expected = Component.text("Player Name: 4drian3d");
        final Component result = MiniMessage.miniMessage().deserialize("Player Name: <example_name>", expansion.audiencePlaceholders(player));

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Relational Placeholder Test")
    void relationalExpansionPlaceholdersTest(){
        Player p1 = new TestPlayer("PlayerOne");
        Player p2 = new TestPlayer("PlayerTwo");

        Expansion expansion = Expansion.builder("relational")
            .relationalPlaceholder(RelationalPlaceholder.create(
                "enemy", (p, o) -> this.isEnemy(p,o) ? Component.text("Enemy", NamedTextColor.RED) : Component.text("Neutral", NamedTextColor.GREEN)))
            .build();

        assertEquals(MiniMessage.miniMessage().deserialize("You are <red>Enemy"), MiniMessage.miniMessage().deserialize("You are <relational_rel_enemy>", expansion.relationalPlaceholders(p1, p2)));
    }

    @Test
    @DisplayName("Global Placeholder Test")
    void globalExpansionPlaceholderTest(){
        ProxyServer proxy = mock(ProxyServer.class);
        when(proxy.getAllPlayers()).thenReturn(Set.of());

        Expansion expansion = Expansion.builder("global")
            .globalPlaceholder("players", Tag.selfClosingInserting(Component.text(proxy.getAllPlayers().size())))
            .build();

        final Component expected = Component.text("Online players: 0");
        final Component actual = MiniMessage.miniMessage().deserialize("Online players: <global_players>", expansion.globalPlaceholders());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registered Placeholders")
    void instancePlaceholdersTest(){
        Expansion.builder("instance")
            .globalPlaceholder("hello", Component.text("hello", NamedTextColor.RED))
            .build()
            .register();

        Component expected = Component.text("hello player", NamedTextColor.RED);
        Component actual = MiniMessage.miniMessage().deserialize("<instance_hello> player", MiniPlaceholders.getGlobalPlaceholders());

        assertContentEquals(expected, actual);
    }

    private boolean isEnemy(Audience player, Audience otherplayer){
        if(player instanceof Player & otherplayer instanceof Player){
            return ((Player)player).getUsername().equals("PlayerOne") && ((Player)otherplayer).getUsername().equals("PlayerTwo");
        }
        return false;
    }

    private void assertContentEquals(Component first, Component second){
        String firstSerialized = PlainTextComponentSerializer.plainText().serialize(first);
        String secondSerialized = PlainTextComponentSerializer.plainText().serialize(second);

        assertEquals(firstSerialized, secondSerialized);
    }

}
