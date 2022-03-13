package me.dreamerzero.miniplaceholders.common;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class PlaceholdersCommand<A> {
    private final @NotNull Supplier<@NotNull Collection<@NotNull String>> playersSuggestions;
    private final @NotNull Function<@NotNull String, @Nullable Audience> toAudience;
    private Function<A, Audience> fromAToAudience = a -> Audience.empty();
    private final Expansion commandExpansion = Expansion.builder("placeholders")
        .globalPlaceholder("count", (queue, ctx) -> Tag.selfClosingInserting(Component.text(MiniPlaceholders.getExpansionCount())))
        .globalPlaceholder("version", (queue, ctx) -> Tag.selfClosingInserting(Component.text(PluginConstants.VERSION)))
        .build();

    public PlaceholdersCommand(Supplier<Collection<String>> playersSuggestions, Function<@NotNull String, @Nullable Audience> toAudience){
        this.playersSuggestions = playersSuggestions;
        this.toAudience = toAudience;
    }

    public PlaceholdersCommand(Supplier<Collection<String>> playersSuggestions, Function<@NotNull String, @Nullable Audience> toAudience, Function<A, Audience> fromAToAudience){
        this(playersSuggestions, toAudience);
        this.fromAToAudience = fromAToAudience;
    }

    private static final Component HEADER = miniMessage().deserialize("<gradient:aqua:white:aqua>---------- <gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient> -----------</gradient>");
    private static final Component FOOTER = miniMessage().deserialize("<gradient:aqua:white:aqua> ---------------       ---------------</gradient>");

    public LiteralArgumentBuilder<A> placeholderTestBuilder(String commandName){
        return LiteralArgumentBuilder.<A>literal(commandName)
            .requires(a -> getAudience(a).pointers().supports(PermissionChecker.POINTER))
            .executes(cmd -> {
                Audience source = getAudience(cmd.getSource());
                if(source.pointers().get(PermissionChecker.POINTER).orElse(null).test("miniplaceholders.command")){
                    source.sendMessage(
                        Component.text()
                            .append(HEADER).append(Component.newline())
                            .append(miniMessage().deserialize("<gradient:aqua:#918fff>Placeholders Available:</gradient> <aqua><placeholders_count></aqua>", commandExpansion.globalPlaceholders())).append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                } else {
                    source.sendMessage(
                        Component.text()
                            .append(HEADER).append(Component.newline())
                            .append(miniMessage().deserialize("<gradient:red:dark_red>Version: <placeholders_version>")).append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                }
                return 1;
            })
            .then(LiteralArgumentBuilder.<A>literal("help")
                .executes(cmd -> {
                    getAudience(cmd.getSource()).sendMessage(
                        Component.text()
                            .append(HEADER).append(Component.newline())
                            .append(miniMessage().deserialize("<gradient:#9694ff:#5269ff>Commands:</gradient>")).append(Component.newline())
                            .append(miniMessage().deserialize("<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>help</aqua>")).append(Component.newline())
                            .append(miniMessage().deserialize("<gradient:aqua:#94d1ff>/miniplaceholders</gradient> <aqua>parse</aqua> <color:#8fadff><player:me></color> <color:#99ffb6><player></color>")).append(Component.newline())
                            .append(FOOTER)
                        .build()
                    );
                    return 1;
                })
            )
            .then(LiteralArgumentBuilder.<A>literal("parse")
                .then(LiteralArgumentBuilder.<A>literal("me")
                    .then(RequiredArgumentBuilder.<A, String>argument("selfStringToParse", StringArgumentType.string())
                        .executes(cmd -> {
                            String stringToParse = cmd.getArgument("selfStringToParse", String.class);
                            Component parsed = this.parseGlobal(stringToParse, getAudience(cmd.getSource()));
                            getAudience(cmd.getSource()).sendMessage(parsed);
                            return 1;
                        })
                    )
                )
                .then(LiteralArgumentBuilder.<A>literal("player")
                    .then(RequiredArgumentBuilder.<A, String>argument("source", StringArgumentType.word())
                        .suggests((argument, builder) -> {
                            playersSuggestions.get().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .then(RequiredArgumentBuilder.<A, String>argument("playerStringToParse", StringArgumentType.string())
                            .executes(cmd -> {
                                Audience objetive = toAudience.apply(cmd.getArgument("source", String.class));
                                if(objetive == null){
                                    getAudience(cmd.getSource()).sendMessage(Component.text("You must specify a valid player", NamedTextColor.RED));
                                    return 1;
                                }
                                String stringToParse = cmd.getArgument("playerStringToParse", String.class);

                                Component parsed = this.parseGlobal(stringToParse, objetive);
                                getAudience(cmd.getSource()).sendMessage(parsed);
                                return 1;
                            })
                        )
                    )
                )
            );
    }

    public LiteralCommandNode<A> placeholderTestCommand(String command){
        return this.placeholderTestBuilder(command).build();
    }

    private Component parseGlobal(String string, Audience audience){
        return miniMessage().deserialize(
            string,
            TagResolver.resolver(
                MiniPlaceholders.getGlobalPlaceholders(),
                MiniPlaceholders.getAudiencePlaceholders(audience)
            )
        );
    }

    private Audience getAudience(A possibleAudience){
        if(possibleAudience instanceof Audience audience){
            return audience;
        }
        Audience audience = fromAToAudience.apply(possibleAudience);
        return audience != null ? audience : Audience.empty();
    }
}