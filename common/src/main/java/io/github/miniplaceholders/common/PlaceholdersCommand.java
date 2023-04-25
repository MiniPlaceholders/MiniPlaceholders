package io.github.miniplaceholders.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
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

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class PlaceholdersCommand<A> {
    private final Supplier<Collection<String>> playersSuggestions;
    private final Function<String, Audience> toAudience;
    private final Function<A, Audience> sourceToAudience;
    private final BiPredicate<A, String> hasPermission;

    private PlaceholdersCommand(
            final Supplier<Collection<String>> playersSuggestions,
            final Function<String, Audience> toAudience,
            final Function<A, Audience> sourceToAudience,
            final BiPredicate<A, String> hasPermission
    ) {
        this.playersSuggestions = playersSuggestions;
        this.toAudience = toAudience;
        this.sourceToAudience = sourceToAudience;
        this.hasPermission = hasPermission;
    }

    private static final Component HELP = text()
            .append(newline())
            .append(miniMessage()
                    .deserialize("<gradient:aqua:white:aqua><st><b>             </st> <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> <st><b>             </st>"))
            .append(newline())
            .append(miniMessage()
                    .deserialize(
                            "<gradient:#9694ff:#5269ff>Commands:</gradient>"))
            .append(newline())
            .append(miniMessage()
                    .deserialize(
                            "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>"))
            .append(newline())
            .append(miniMessage()
                    .deserialize(
                            "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <#8fadff><player | me></#8fadff> <#99ffb6><player></#99ffb6>"))
            .append(newline())
            .append(miniMessage()
                    .deserialize("<gradient:aqua:white:aqua><st><b>                                                 "))
            .build();

    private static final Component INFO = AboutMenu.builder()
            .title(miniMessage().deserialize("<gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient>"))
            .description(text("MiniMessage Component-based Placeholders for PaperMC, Folia, Fabric, Velocity and Krypton platforms"))
            .credits("Author", AboutMenu.Credit.of("4drian3d").url("https://github.com/4drian3d"))
            .credits("Contributors", AboutMenu.Credit.of("Sliman4"))
            .buttons(
                    AboutMenu.Link.of("https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/User-Getting-Started").text("Documentation").icon("\uD83D\uDCD6"),
                    AboutMenu.Link.of("https://discord.gg/5NMMzK5mAn").text("Discord").color(TextColor.color(0x7289da)).icon("⭐"),
                    AboutMenu.Link.of("https://modrinth.com/plugin/miniplaceholders").text("Downloads").color(TextColor.color(0xff496e)).icon("↓")
            ).version(Version.fromString(PluginConstants.VERSION))
            .build()
            .toComponent();

    public LiteralArgumentBuilder<A> asBuilder(String commandName){
        return LiteralArgumentBuilder.<A>literal(commandName)
            .requires(a -> permissionValue(a, "miniplaceholders.command") != TriState.FALSE)
            .executes(cmd -> {
                Audience source = getAudience(cmd.getSource());
                source.sendMessage(INFO);
                return Command.SINGLE_SUCCESS;
            })
            .then(LiteralArgumentBuilder.<A>literal("help")
                .requires(src -> permissionValue(src, "miniplaceholders.command.help").toBooleanOrElse(false))
                .executes(cmd -> {
                    getAudience(cmd.getSource()).sendMessage(HELP);
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(LiteralArgumentBuilder.<A>literal("parse")
                .requires(cmd -> permissionValue(cmd, "miniplaceholders.command.parse").toBooleanOrElse(false))
                .then(LiteralArgumentBuilder.<A>literal("me")
                    .requires(cmd -> permissionValue(cmd, "miniplaceholders.command.parse.me").toBooleanOrElse(false))
                    .then(RequiredArgumentBuilder.<A, String>argument("string", string())
                        .executes(cmd -> {
                            final String toParse = getString(cmd, "string");
                            final Component parsed = parseGlobal(toParse, getAudience(cmd.getSource()));
                            getAudience(cmd.getSource()).sendMessage(parsed);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
                .then(LiteralArgumentBuilder.<A>literal("player")
                    .requires(cmd -> permissionValue(cmd, "miniplaceholders.command.parse.player").toBooleanOrElse(false))
                    .then(RequiredArgumentBuilder.<A, String>argument("source", word())
                        .suggests((argument, builder) -> supplyAsync(() -> {
                                playersSuggestions.get().forEach(builder::suggest);
                                return builder.build();
                        }))
                        .then(RequiredArgumentBuilder.<A, String>argument("string", string())
                            .executes(cmd -> {
                                final Audience objective = toAudience.apply(getString(cmd, "source"));
                                if (objective == null){
                                    getAudience(cmd.getSource()).sendMessage(text("You must specify a valid player", RED));
                                    return -1;
                                }
                                final String toParse = cmd.getArgument("string", String.class);

                                final Component parsed = parseGlobal(toParse, objective);
                                getAudience(cmd.getSource()).sendMessage(parsed);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
            );
    }

    public LiteralCommandNode<A> asNode(String command) {
        return asBuilder(command).build();
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

    private Audience getAudience(final A possibleAudience) {
        if (possibleAudience instanceof Audience audience) {
            return audience;
        }
        final Audience audience = sourceToAudience.apply(possibleAudience);
        return audience != null ? audience : Audience.empty();
    }

    private TriState permissionValue(final A source, final String permission) {
        if (hasPermission != null) {
            return TriState.byBoolean(hasPermission.test(source, permission));
        }
        return getAudience(source)
                .get(PermissionChecker.POINTER)
                .map(checker -> checker.value(permission))
                .orElse(TriState.FALSE);
    }

    public static <A> Builder<A> builder() {
        return new Builder<>();
    }

    public final static class Builder<A> implements AbstractBuilder<PlaceholdersCommand<A>> {
        private Supplier<Collection<String>> playersSuggestions;
        private Function<String, Audience> toAudience;
        private Function<A, Audience> fromAToAudience = a -> Audience.empty();
        private BiPredicate<A, String> hasPermission;

        private Builder() {}

        public Builder<A> playerSuggestions(final Supplier<@NotNull Collection<@NotNull String>> playersSuggestions) {
            this.playersSuggestions = playersSuggestions;
            return this;
        }

        public Builder<A> toAudience(final @NotNull Function<@NotNull String, @Nullable Audience> argumentToAudience) {
            this.toAudience = argumentToAudience;
            return this;
        }

        public Builder<A> fromSourceToAudience(final @NotNull Function<@NotNull A, @Nullable Audience> sourceToAudience) {
            this.fromAToAudience = sourceToAudience;
            return this;
        }

        public Builder<A> hasPermissionCheck(final @NotNull BiPredicate<@NotNull A, @NotNull String> hasPermission) {
            this.hasPermission = hasPermission;
            return this;
        }

        @Override
        public @NotNull PlaceholdersCommand<A> build() {
            return new PlaceholdersCommand<>(
                    requireNonNull(playersSuggestions),
                    requireNonNull(toAudience),
                    requireNonNull(fromAToAudience),
                    hasPermission
            );
        }
    }
}