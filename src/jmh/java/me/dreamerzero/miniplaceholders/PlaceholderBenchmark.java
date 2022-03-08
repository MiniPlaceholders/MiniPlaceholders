package me.dreamerzero.miniplaceholders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import me.dreamerzero.miniplaceholders.velocity.Expansion;
import me.dreamerzero.miniplaceholders.velocity.MiniPlaceholders;
import me.dreamerzero.miniplaceholders.velocity.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.velocity.placeholder.GlobalPlaceholder;

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
            .audiencePlaceholder(AudiencePlaceholder.create(
                "name", p -> Tag.selfClosingInserting(Component.text(((Player)p).getUsername()))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "uuid", p -> Tag.selfClosingInserting(Component.text(((Player)p).getUniqueId().toString()))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablist", p -> Tag.selfClosingInserting(((Player)p).getPlayerListHeader())
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "aea", p -> Tag.selfClosingInserting(Component.text("aea"))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "tablistfooter", p ->Tag.selfClosingInserting(Component.text("footer"))
            ));

        Expansion expansionBuilded = expansion.build();
        expansionBuilded.audiencePlaceholders(player);
        final TagResolver resolvers = MiniPlaceholders.getAudiencePlaceholders(player);

        MiniMessage.miniMessage().deserialize("Player Name: <benchmark-name>", resolvers);
    }

    @Benchmark
    public void singleBench(){
        Expansion.builder("single")
            .globalPlaceholder(GlobalPlaceholder.create("servername", Tag.selfClosingInserting(Component.text("Peruviankkit"))))
            .build()
            .register();

        TagResolver resolvers = MiniPlaceholders.getGlobalPlaceholders();

        MiniMessage.miniMessage().deserialize("Server name: <single-servername>", resolvers);
    }

}
