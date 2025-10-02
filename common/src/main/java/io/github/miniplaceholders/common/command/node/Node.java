package io.github.miniplaceholders.common.command.node;

import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface Node permits ExpansionsNode, HelpNode, ParseNode, RootNode {
  boolean hasPermission(Audience audience);

  String permission();
}
