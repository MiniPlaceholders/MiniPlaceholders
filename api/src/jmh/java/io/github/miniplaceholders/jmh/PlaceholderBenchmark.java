package io.github.miniplaceholders.jmh;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PlaceholderBenchmark {
  @Benchmark
  public void placeholderBench() {
    BenchAudience player = new BenchAudience("4drian3d");
    Expansion.Builder expansion = Expansion.builder("benchmark")
            .audiencePlaceholder(
                    BenchAudience.class,
                    "name",
                    (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(aud.getName()))
            )
            .audiencePlaceholder(
                    BenchAudience.class,
                    "uuid",
                    (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(aud.getUUID().toString()))
            )
            .audiencePlaceholder(
                    "tablistfooter",
                    (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text("footer"))
            );

    Expansion expansionBuilded = expansion.build();
    expansionBuilded.register();
    final TagResolver resolvers = MiniPlaceholders.audiencePlaceholders();

    MiniMessage.miniMessage().deserialize("Player Name: <benchmark_name>", player, resolvers);
  }

  @Benchmark
  public void singleBench() {
    Expansion.builder("single")
            .globalPlaceholder("servername",
                    (queue, ctx) -> Tag.selfClosingInserting(Component.text("Peruviankkit")))
            .build()
            .register();

    TagResolver resolvers = MiniPlaceholders.globalPlaceholders();

    MiniMessage.miniMessage().deserialize("Server name: <single_servername>", resolvers);
  }

}
