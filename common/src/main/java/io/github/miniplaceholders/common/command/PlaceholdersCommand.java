package io.github.miniplaceholders.common.command;

import io.github.miniplaceholders.common.command.node.ExpansionsNode;
import io.github.miniplaceholders.common.command.node.HelpNode;
import io.github.miniplaceholders.common.command.node.ParseNode;
import io.github.miniplaceholders.common.command.node.RootNode;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public record PlaceholdersCommand<A extends Audience>(
        PlayerSuggestions playersSuggestions,
        AudienceConverter audienceConverter,
        PermissionTester<A> hasPermission,
        String command,
        CommandManager<A> commandManager
) {

    public static final Component TITLE = miniMessage().deserialize("<gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient>");
    public static final Component HEADER = miniMessage().deserialize(
            "<gradient:aqua:white:aqua><st><b>             </st> <title> <st><b>             </st>", Placeholder.component("title", TITLE));
    public static final Component FOOTER = miniMessage().deserialize(
            "<gradient:aqua:white:aqua><st><b>                                                 ");

    public void register() {
      //noinspection unchecked
      MinecraftExceptionHandler.create(AudienceProvider.nativeAudience())
                .defaultHandlers()
                .decorator(component -> Component.text().append(TITLE).appendSpace().append(component))
                .registerTo((CommandManager<Audience>) commandManager);

      // Nodes
      Stream.of(new RootNode<>(hasPermission), new HelpNode<A>(), new ParseNode<A>(playersSuggestions, audienceConverter), new ExpansionsNode<A>())
              .map(handler -> handler.apply(commandManager.commandBuilder(command)))
              .forEach(commandManager::command);
    }

    public static <A extends Audience> Builder<A> builder() {
        return new Builder<>();
    }

    public final static class Builder<A extends Audience> implements AbstractBuilder<PlaceholdersCommand<A>> {
        private PlayerSuggestions playersSuggestions;
        private AudienceConverter audienceConverter;
        private PermissionTester<A> hasPermission;
        private String command;
        private CommandManager<A> commandManager;

        private Builder() {}

        public Builder<A> playerSuggestions(final @NotNull PlayerSuggestions playersSuggestions) {
            this.playersSuggestions = playersSuggestions;
            return this;
        }

        public Builder<A> toAudience(final @NotNull AudienceConverter audienceConverter) {
            this.audienceConverter = audienceConverter;
            return this;
        }

        public Builder<A> hasPermissionCheck(final @Nullable PermissionTester<A> hasPermission) {
            this.hasPermission = hasPermission;
            return this;
        }

        public Builder<A> command(final @NotNull String command) {
            this.command = command;
            return this;
        }

        public Builder<A> manager(final @NotNull CommandManager<A> commandManager) {
            this.commandManager = commandManager;
            return this;
        }

        @Override
        public @NotNull PlaceholdersCommand<A> build() {
            return new PlaceholdersCommand<>(
                    requireNonNull(playersSuggestions),
                    requireNonNull(audienceConverter),
                    hasPermission,
                    requireNonNull(command),
                    requireNonNull(commandManager)
            );
        }
    }
}