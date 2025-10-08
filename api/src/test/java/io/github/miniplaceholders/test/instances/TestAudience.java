package io.github.miniplaceholders.test.instances;

import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TestAudience(String name) implements Audience {}
