package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;

@FunctionalInterface
public interface PlaceholderCommandExecutor {
  void execute(Audience audience);
}
