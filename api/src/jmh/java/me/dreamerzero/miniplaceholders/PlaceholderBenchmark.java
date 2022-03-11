package me.dreamerzero.miniplaceholders;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;

@State(Scope.Benchmark)
public class PlaceholderBenchmark {
    @Benchmark
    public void placeholderBench(){
        BenchAudience player = new BenchAudience("4drian3d");
        Expansion.Builder expansion = Expansion.builder("benchmark")
            .audiencePlaceholder(
                "name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((BenchAudience)aud).getName()))
            )
            .audiencePlaceholder(
                "uuid", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((BenchAudience)aud).getUUID().toString()))
            )
            .audiencePlaceholder(
                "tablistfooter", (aud, queue, ctx) ->Tag.selfClosingInserting(Component.text("footer"))
            );

        Expansion expansionBuilded = expansion.build();
        expansionBuilded.audiencePlaceholders(player);
        final TagResolver resolvers = MiniPlaceholders.getAudiencePlaceholders(player);

        MiniMessage.miniMessage().deserialize("Player Name: <benchmark-name>", resolvers);
    }

    @Benchmark
    public void singleBench(){
        Expansion.builder("single")
            .globalPlaceholder("servername", (queue, ctx) -> Tag.selfClosingInserting(Component.text("Peruviankkit")))
            .build()
            .register();

        TagResolver resolvers = MiniPlaceholders.getGlobalPlaceholders();

        MiniMessage.miniMessage().deserialize("Server name: <single-servername>", resolvers);
    }

}
