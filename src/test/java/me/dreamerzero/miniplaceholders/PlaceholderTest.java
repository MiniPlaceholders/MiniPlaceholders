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
import me.dreamerzero.miniplaceholders.velocity.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
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
            .audiencePlaceholder("name", p -> Component.text(((Player)p).getUsername()))
            .audiencePlaceholder("uuid", p->Component.text(((Player)p).getUniqueId().toString()))
            .audiencePlaceholder("tablist", p->Component.text("tablist"))
            .audiencePlaceholder("aea", p -> Component.text("aea"))
            .audiencePlaceholder("tablistfooter", p -> Component.text("footer"))
            .globalPlaceholder("playercount", Tag.selfClosingInserting(Component.text("Online Players: "+proxy.getAllPlayers().size())))
            .relationalPlaceholder("isEnemy", (p,o) ->
                this.isEnemy(p,o) ? Component.text("Enemy", NamedTextColor.RED) : Component.text("Neutral", NamedTextColor.GREEN))
            .build();

        final TagResolver resolvers = TagResolver.resolver(expansion.audiencePlaceholders(player), expansion.globalPlaceholders());

        final Component expected = Component.text("Player Name: 4drian3d, Online Players: 0");
        final Component result = MiniMessage.miniMessage().deserialize("Player Name: <example_name>, <example_playercount>", resolvers);

        assertEquals(expected, result);

        expansion.register();

        assertEquals(Component.text("Online Players: 0"), MiniMessage.miniMessage().deserialize("<example_playercount>", MiniPlaceholders.getGlobalPlaceholders()));
    }

    private boolean isEnemy(Audience player, Audience otherplayer){
        if(player instanceof Player & otherplayer instanceof Player){
            return ((Player)player).getUsername().equals("Pepito") && ((Player)otherplayer).getUsername().equals("Juanito");
        }
        return false;
    }

}
