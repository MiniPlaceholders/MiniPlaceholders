package io.github.miniplaceholders.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class PlaceholdersCommand<A> {
    private final Supplier<Collection<String>> playersSuggestions;
    private final Function<String, Audience> toAudience;
    private final Function<A, Audience> sourceToAudience;
    private final BiPredicate<A, String> hasPermission;
    private final Expansion commandExpansion = Expansion.builder("placeholders")
        .globalPlaceholder("count", (queue, ctx) -> Tag.selfClosingInserting(text(MiniPlaceholders.getExpansionCount())))
        .globalPlaceholder("version", (queue, ctx) -> Tag.selfClosingInserting(text(PluginConstants.VERSION)))
        .build();

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

    private static final Component HEADER = miniMessage()
            .deserialize("<gradient:aqua:white:aqua>---------- <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> -----------</gradient>");
    private static final Component FOOTER = miniMessage()
            .deserialize("<gradient:aqua:white:aqua> ---------------       ---------------</gradient>");

    public LiteralArgumentBuilder<A> asBuilder(String commandName){
        return LiteralArgumentBuilder.<A>literal(commandName)
            .requires(a -> getAudience(a).pointers().supports(PermissionChecker.POINTER))
            .executes(cmd -> {
                Audience source = getAudience(cmd.getSource());
                if (hasPermission(cmd.getSource(), "miniplaceholders.command")) {
                    source.sendMessage(
                        text()
                            .append(HEADER)
                            .append(Component.newline())
                            .append(miniMessage()
                                    .deserialize(
                                            "<gradient:aqua:#918fff>Placeholders Available:</gradient> <aqua><placeholders_count></aqua>",
                                            commandExpansion.globalPlaceholders()
                                    )).append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                } else {
                    source.sendMessage(
                        text()
                            .append(HEADER)
                            .append(Component.newline())
                            .append(miniMessage()
                                    .deserialize(
                                            "<gradient:#8693AB:#BDD4E7>Version: <placeholders_version>",
                                            commandExpansion.globalPlaceholders()
                                    )).append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                }
                return 1;
            })
            .then(LiteralArgumentBuilder.<A>literal("help")
                .requires(src -> hasPermission(src, "miniplaceholders.command.help"))
                .executes(cmd -> {
                    getAudience(cmd.getSource()).sendMessage(
                        text()
                            .append(HEADER)
                            .append(Component.newline())
                            .append(miniMessage()
                                    .deserialize(
                                            "<gradient:#9694ff:#5269ff>Commands:</gradient>"))
                                    .append(Component.newline())
                            .append(miniMessage()
                                    .deserialize(
                                            "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>"))
                                    .append(Component.newline())
                            .append(miniMessage()
                                    .deserialize(
                                            "<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <#8fadff><player|me></#8fadff> <#99ffb6><player></#99ffb6>"))
                                    .append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(LiteralArgumentBuilder.<A>literal("parse")
                .requires(cmd -> hasPermission(cmd, "miniplaceholders.command.parse"))
                .then(LiteralArgumentBuilder.<A>literal("me")
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
        if (possibleAudience instanceof Audience audience){
            return audience;
        }
        final Audience audience = sourceToAudience.apply(possibleAudience);
        return audience != null ? audience : Audience.empty();
    }

    private boolean hasPermission(final A source, final String permission) {
        if (hasPermission != null) {
            return hasPermission.test(source, permission);
        }
        return getAudience(source)
                .get(PermissionChecker.POINTER)
                .map(checker -> checker.test(permission))
                .orElse(false);
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