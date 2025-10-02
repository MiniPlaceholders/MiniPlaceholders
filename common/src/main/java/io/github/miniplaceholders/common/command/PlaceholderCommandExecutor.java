package io.github.miniplaceholders.common.command;

import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface PlaceholderCommandExecutor {
  void execute(@NonNull Audience audience);
}
