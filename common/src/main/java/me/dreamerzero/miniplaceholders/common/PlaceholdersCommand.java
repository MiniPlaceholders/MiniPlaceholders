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

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class PlaceholdersCommand<A extends Audience> {
    private final @NotNull Supplier<@NotNull Collection<@NotNull String>> playersSuggestions;
    private final @NotNull Function<@NotNull String, @Nullable Audience> toAudience;

    public PlaceholdersCommand(Supplier<Collection<String>> playersSuggestions, Function<@NotNull String, @Nullable Audience> toAudience){
        this.playersSuggestions = playersSuggestions;
        this.toAudience = toAudience;
    }

    public LiteralCommandNode<A> placeholderTestCommand(String commandName){
        return LiteralArgumentBuilder.<A>literal(commandName)
            .executes(cmd -> {
                //TODO: Add main command info
                cmd.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<rainbow>Main Command"));
                return 1;
            })
            .then(LiteralArgumentBuilder.<A>literal("help")
                .executes(cmd -> {
                    //TODO: Add help
                    cmd.getSource().sendMessage(MiniMessage.miniMessage().deserialize("<rainbow>Hello"));
                    return 1;
                })
            )
            .then(LiteralArgumentBuilder.<A>literal("parse")
                .then(LiteralArgumentBuilder.<A>literal("me")
                    .then(RequiredArgumentBuilder.<A, String>argument("selfStringToParse", StringArgumentType.string())
                        .executes(cmd -> {
                            String stringToParse = cmd.getArgument("selfStringToParse", String.class);

                            Component parsed = this.parseGlobal(stringToParse, cmd.getSource());
                            cmd.getSource().sendMessage(parsed);
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
                                    cmd.getSource().sendMessage(Component.text("You must specify a valid player"));
                                    return 1;
                                }
                                String stringToParse = cmd.getArgument("playerStringToParse", String.class);

                                Component parsed = this.parseGlobal(stringToParse, objetive);
                                cmd.getSource().sendMessage(parsed);
                                return 1;
                            })
                        )
                    )
                )
            )
        .build();
    }

    private Component parseGlobal(String string, Audience audience){
        return MiniMessage.miniMessage().deserialize(
            string,
            TagResolver.resolver(
                MiniPlaceholders.getGlobalPlaceholders(),
                MiniPlaceholders.getAudiencePlaceholders(audience)
            )
        );
    }
}