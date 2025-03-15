package io.github.miniplaceholders.test.instances;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.audience.Audience;

public record TestAudience(@NotNull String name) implements Audience {}
