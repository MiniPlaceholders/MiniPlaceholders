package io.github.miniplaceholders.common;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.util.TriState;
import net.william278.desertwell.about.AboutMenu;
import net.william278.desertwell.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class PlaceholdersCommand<A extends Audience> {
    private final Supplier<ArrayList<String>> playersSuggestions;
    private final Function<String, Audience> toAudience;
    private final BiPredicate<A, String> hasPermission;
    private final String command;
    private final CommandManager<A> commandManager;

    private PlaceholdersCommand(
            final Supplier<ArrayList<String>> playersSuggestions,
            final Function<String, Audience> toAudience,
            final BiPredicate<A, String> hasPermission,
            final String command,
            final CommandManager<A> commandManager
    ) {
        this.playersSuggestions = playersSuggestions;
        this.toAudience = toAudience;
        this.hasPermission = hasPermission;
        this.command = command;
        this.commandManager = commandManager;
    }

    private static final Component TITLE = miniMessage().deserialize("<gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient>");
    private static final Component HELP = text()
            .append(newline(), miniMessage().deserialize(
                    "<gradient:aqua:white:aqua><st><b>             </st> <title> <st><b>             </st>", Placeholder.component("title", TITLE)))
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
            .title(TITLE)
            .description(text("MiniMessage Component-based Placeholders for Paper, Folia, Fabric, Velocity, Sponge and Krypton platforms"))
            .credits("Author", AboutMenu.Credit.of("4drian3d").url("https://github.com/4drian3d"))
            .credits("Contributors", AboutMenu.Credit.of("Sliman4"))
            .buttons(
                    AboutMenu.Link.of("https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/User-Getting-Started").text("Documentation").icon("⛏"),
                    AboutMenu.Link.of("https://discord.gg/5NMMzK5mAn").text("Discord").color(TextColor.color(0x7289da)).icon("⭐"),
                    AboutMenu.Link.of("https://modrinth.com/plugin/miniplaceholders").text("Downloads").color(TextColor.color(0xff496e)).icon("↓")
            ).version(Version.fromString(PluginConstants.VERSION))
            .build()
            .toComponent();

    private Command.Builder<A> rootBuilder() {
        return commandManager.commandBuilder(command);
    }

    public void register() {
        new MinecraftExceptionHandler<A>()
                .withDefaultHandlers()
                .withDecorator(component -> Component.text().append(TITLE).appendSpace().append(component).build())
                .apply(commandManager, AudienceProvider.nativeAudience());

        commandManager.command(rootBuilder()
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
        final CommandArgument<A, String> sourceArgument = StringArgument.<A>builder("source")
                .single()
                .withDefaultDescription(ArgumentDescription.of("The source from which the message will be parsed"))
                .withSuggestionsProvider((ctx, st) -> {
                    final List<String> suggestions = playersSuggestions.get();
                    suggestions.add("me");
                    return suggestions;
                })
                .build();
        final CommandArgument<A, String> stringArgument = StringArgument.<A>builder("message")
                .greedy()
                .withDefaultDescription(ArgumentDescription.of("Message to be parsed"))
                .build();

        commandManager.command(
                rootBuilder().literal("parse")
                        .argument(sourceArgument)
                        .argument(stringArgument)
                        .permission("miniplaceholders.command.parse")
                        .handler(handler -> {
                            final String source = handler.get(sourceArgument);

                            final Audience objective = "me".equals(source)
                                    ? handler.getSender()
                                    : toAudience.apply(source);
                            if (objective == null){
                                handler.getSender().sendMessage(text("You must specify a valid player", RED));
                                return;
                            }
                            final String toParse = handler.get(stringArgument);

                            final Component parsed = miniMessage().deserialize(
                                    toParse,
                                    MiniPlaceholders.getAudienceGlobalPlaceholders(objective)
                            );
                            handler.getSender().sendMessage(parsed);
                        })
        );
        commandManager.command(
                rootBuilder().literal("help")
                        .permission("miniplaceholders.command.help")
                        .handler(handler -> handler.getSender().sendMessage(HELP))
        );
    }

    public static <A extends Audience> Builder<A> builder() {
        return new Builder<>();
    }

    public final static class Builder<A extends Audience> implements AbstractBuilder<PlaceholdersCommand<A>> {
        private Supplier<ArrayList<String>> playersSuggestions;
        private Function<String, Audience> toAudience;
        private BiPredicate<A, String> hasPermission;
        private String command;
        private CommandManager<A> commandManager;

        private Builder() {}

        public Builder<A> playerSuggestions(final Supplier<@NotNull ArrayList<@NotNull String>> playersSuggestions) {
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