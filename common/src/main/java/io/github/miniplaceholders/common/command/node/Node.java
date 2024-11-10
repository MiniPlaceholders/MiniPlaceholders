package io.github.miniplaceholders.common.command.node;

import net.kyori.adventure.audience.Audience;
import org.incendo.cloud.Command;

public sealed interface Node<S extends Audience> permits ExpansionsNode, HelpNode, ParseNode, RootNode {
  Command.Builder<S> apply(Command.Builder<S> rootBuilder);
}
