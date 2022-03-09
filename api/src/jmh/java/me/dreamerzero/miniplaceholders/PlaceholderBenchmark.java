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
import me.dreamerzero.miniplaceholders.api.placeholder.AudiencePlaceholder;
import me.dreamerzero.miniplaceholders.api.placeholder.GlobalPlaceholder;

@State(Scope.Benchmark)
public class PlaceholderBenchmark {
    @Benchmark
    public void placeholderBench(){
        BenchAudience player = new BenchAudience("4drian3d");
        Expansion.Builder expansion = Expansion.builder("benchmark")
            .audiencePlaceholder(AudiencePlaceholder.create(
                "name", p -> Tag.selfClosingInserting(Component.text(((BenchAudience)p).getName()))
            ))
            .audiencePlaceholder(AudiencePlaceholder.create(
                "uuid", p -> Tag.selfClosingInserting(Component.text(((BenchAudience)p).getUUID().toString()))
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
