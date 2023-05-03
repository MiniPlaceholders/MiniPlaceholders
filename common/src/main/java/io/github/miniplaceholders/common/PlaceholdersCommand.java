package io.github.miniplaceholders.common;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.TriState;
import net.william278.desertwell.about.AboutMenu;
import net.william278.desertwell.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class PlaceholdersCommand<A extends Audience> {
    private final Supplier<Collection<String>> playersSuggestions;
    private final Function<String, Audience> toAudience;
    private final BiPredicate<A, String> hasPermission;
    private final String command;
    private final CommandManager<A> commandManager;

    private PlaceholdersCommand(
            final Supplier<Collection<String>> playersSuggestions,
            final Function<String, Audience> toAudience,
            final BiPredicate<A, String> hasPermission,
            String command,
            CommandManager<A> commandManager
    ) {
        this.playersSuggestions = playersSuggestions;
        this.toAudience = toAudience;
        this.hasPermission = hasPermission;
        this.command = command;
        this.commandManager = commandManager;
    }

    private static final Component HELP = text()
            .append(newline(), miniMessage().deserialize(
                    "<gradient:aqua:white:aqua><st><b>             </st> <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> <st><b>             </st>"))
            .append(newline(), miniMessage().deserialize(
                    "<gradient:#9694ff:#5269ff>Commands:</gradient>"))
            .append(newline(), miniMessage().deserialize(
                    "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>"))
            .append(newline(), miniMessage().deserialize(
                    "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <#8fadff><player | me></#8fadff> <#99ffb6><player></#99ffb6>"))
            .append(newline(), miniMessage().deserialize(
                    "<gradient:aqua:white:aqua><st><b>                                                 "))
            .build();

    private static final Component INFO = AboutMenu.builder()
            .title(miniMessage().deserialize("<gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient>"))
            .description(text("MiniMessage Component-based Placeholders for PaperMC, Folia, Fabric, Velocity and Krypton platforms"))
            .credits("Author", AboutMenu.Credit.of("4drian3d").url("https://github.com/4drian3d"))
            .credits("Contributors", AboutMenu.Credit.of("Sliman4"))
            .buttons(
                    AboutMenu.Link.of("https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/User-Getting-Started").text("Documentation").icon("⛏"),
                    AboutMenu.Link.of("https://discord.gg/5NMMzK5mAn").text("Discord").color(TextColor.color(0x7289da)).icon("⭐"),
                    AboutMenu.Link.of("https://modrinth.com/plugin/miniplaceholders").text("Downloads").color(TextColor.color(0xff496e)).icon("↓")
            ).version(Version.fromString(PluginConstants.VERSION))
            .build()
            .toComponent();

    private cloud.commandframework.Command.Builder<A> abuilder() {
        return commandManager.commandBuilder(command);
    }

    public void register() {
        commandManager.command(abuilder()
                .permission(src -> {
                    if (hasPermission != null) {
                        return hasPermission.test(src, "miniplaceholders.command");
                    }
                    return src
                            .get(PermissionChecker.POINTER)
                            .map(checker -> checker.value("miniplaceholders.command"))
                            .orElse(TriState.FALSE) != TriState.FALSE;
                })
                .handler(handler -> handler.getSender().sendMessage(INFO)));
        final StringArgument<A> sourceArgument = StringArgument.<A>builder("source")
                .single()
                .withSuggestionsProvider((ctx, st) -> new ArrayList<>(playersSuggestions.get()))
                .build();
        final StringArgument<A> stringArgument = StringArgument.<A>builder("string")
                .greedy()
                .build();

        commandManager.command(
                abuilder().literal("parse")
                        .argument(sourceArgument)
                        .argument(StringArgument.<A>builder("string").greedy().build())
                        .permission("miniplaceholders.command.parse")
                        .handler(handler -> {
                            final String source = handler.get(sourceArgument);

                            final Audience objective;
                            if ("me".equals(source)) {
                                objective = handler.getSender();
                            } else {
                                objective = toAudience.apply(source);
                            }
                            if (objective == null){
                                handler.getSender().sendMessage(text("You must specify a valid player", RED));
                                return;
                            }
                            final String toParse = handler.get(stringArgument);

                            final Component parsed = parseGlobal(toParse, objective);
                            handler.getSender().sendMessage(parsed);
                        })
        );
        commandManager.command(
                abuilder().literal("help")
                        .permission("miniplaceholders.command.help")
                        .handler(handler -> handler.getSender().sendMessage(HELP))
        );
    }

    private Component parseGlobal(final String string, final Audience audience) {
        return miniMessage().deserialize(
            string,
            TagResolver.resolver(
                MiniPlaceholders.getGlobalPlaceholders(),
                MiniPlaceholders.getAudiencePlaceholders(audience)
            )
        );
    }

    public static <A extends Audience> Builder<A> builder() {
        return new Builder<>();
    }

    public final static class Builder<A extends Audience> implements AbstractBuilder<PlaceholdersCommand<A>> {
        private Supplier<Collection<String>> playersSuggestions;
        private Function<String, Audience> toAudience;
        private BiPredicate<A, String> hasPermission;
        private String command;
        private CommandManager<A> commandManager;

        private Builder() {}

        public Builder<A> playerSuggestions(final Supplier<@NotNull Collection<@NotNull String>> playersSuggestions) {
            this.playersSuggestions = playersSuggestions;
            return this;
        }

        public Builder<A> toAudience(final @NotNull Function<@NotNull String, @Nullable Audience> argumentToAudience) {
            this.toAudience = argumentToAudience;
            return this;
        }

        public Builder<A> hasPermissionCheck(final @NotNull BiPredicate<@NotNull A, @NotNull String> hasPermission) {
            this.hasPermission = hasPermission;
            return this;
        }

        public Builder<A> command(final String command) {
            this.command = command;
            return this;
        }

        public Builder<A> manager(CommandManager<A> commandManager) {
            this.commandManager = commandManager;
            return this;
        }

        @Override
        public @NotNull PlaceholdersCommand<A> build() {
            return new PlaceholdersCommand<>(
                    requireNonNull(playersSuggestions),
                    requireNonNull(toAudience),
                    hasPermission,
                    command,
                    commandManager
            );
        }
    }
}