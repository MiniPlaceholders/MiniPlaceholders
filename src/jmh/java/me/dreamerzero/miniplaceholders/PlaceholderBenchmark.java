package me.dreamerzero.miniplaceholders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import me.dreamerzero.miniplaceholders.velocity.Expansion;
import me.dreamerzero.miniplaceholders.velocity.MiniPlaceholders;

@State(Scope.Benchmark)
public class PlaceholderBenchmark {
    @Benchmark
    public void placeholderBench(){
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("4drian3d");
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getPlayerListHeader()).thenReturn(Component.text("a"));
        when(player.getPlayerListFooter()).thenReturn(Component.text("b"));
        Expansion.Builder expansion = Expansion.builder("benchmark")
            .addAudiencePlaceholder("name", p -> Component.text(((Player)p).getUsername()))
            .addAudiencePlaceholder("uuid", p->Component.text(((Player)p).getUniqueId().toString()))
            .addAudiencePlaceholder("tablist", p -> Component.text("tablist"))
            .addAudiencePlaceholder("aea", p -> Component.text("aea"))
            .addAudiencePlaceholder("tablistfooter", p -> Component.text("footer"))
        ;

        Expansion expansionBuilded = expansion.build();
        expansionBuilded.getAudiencePlaceholders(player);
        final TagResolver resolvers = MiniPlaceholders.getAudiencePlaceholders(player);

        MiniMessage.miniMessage().deserialize("Player Name: <benchmark-name>", resolvers);
    }

    @Benchmark
    public void singleBench(){
        Expansion.builder("single")
            .addGlobalPlaceholder("servername", () -> Component.text("Peruviankkit")).build()
        ;

        TagResolver resolvers = MiniPlaceholders.getGlobalPlaceholders();

        MiniMessage.miniMessage().deserialize("Server name: <single-servername>", resolvers);
    }

}
