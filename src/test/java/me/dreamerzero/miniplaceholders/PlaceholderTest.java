package me.dreamerzero.miniplaceholders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import me.dreamerzero.miniplaceholders.velocity.Expansion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class PlaceholderTest {

    @Test
    @DisplayName("Placeholder Test")
    void placeholdersTest(){
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("4drian3d");
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getPlayerListHeader()).thenReturn(Component.text("a"));
        when(player.getPlayerListFooter()).thenReturn(Component.text("b"));

        ProxyServer proxy = mock(ProxyServer.class);

        when(proxy.getAllPlayers()).thenReturn(Set.of());


        Expansion expansion = Expansion.builder("example")
            .addAudiencePlaceholder("name", p -> Component.text(((Player)p).getUsername()))
            .addAudiencePlaceholder("uuid", p->Component.text(((Player)p).getUniqueId().toString()))
            .addAudiencePlaceholder("tablist", p->Component.text("tablist"))
            .addAudiencePlaceholder("aea", p -> Component.text("aea"))
            .addAudiencePlaceholder("tablistfooter", p -> Component.text("footer"))
            .addGlobalPlaceholder("playercount", ()->Component.text("Online Players: "+proxy.getAllPlayers().size()))
            .addRelationalPlaceholder("isEnemy", (p,o) ->
                isEnemy(p,o) ? Component.text("Enemy", NamedTextColor.RED) : Component.text("Neutral", NamedTextColor.GREEN))
            .build();

        final TagResolver resolvers = expansion.getAudiencePlaceholders(player);

        final Component expected = Component.text("Player Name: 4drian3d");
        final Component result = MiniMessage.miniMessage().deserialize("Player Name: <example-name>", resolvers);

        assertEquals(expected, result);
    }

    private boolean isEnemy(Audience player, Audience otherplayer){
        if(player instanceof Player & otherplayer instanceof Player){
            return ((Player)player).getUsername().equals("Pepito") && ((Player)otherplayer).getUsername().equals("Juanito");
        }
        return false;
    }

}
