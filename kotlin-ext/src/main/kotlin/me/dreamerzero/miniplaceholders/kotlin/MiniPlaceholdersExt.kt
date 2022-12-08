package me.dreamerzero.miniplaceholders.kotlin

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders.*
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.jetbrains.annotations.NotNull

public fun globalPlaceholders(): @NotNull TagResolver
    = getGlobalPlaceholders()

public fun audiencePlaceholders(@NotNull audience: Audience): @NotNull TagResolver
    = getAudiencePlaceholders(audience)

public fun relationalPlaceholders(@NotNull audience: Audience, @NotNull other: Audience) : @NotNull TagResolver
    = getRelationalPlaceholders(audience, other)

public fun audienceGlobalPlaceholders(@NotNull audience: Audience): @NotNull TagResolver
    = getAudienceGlobalPlaceholders(audience)

public fun relationalGlobalPlaceholders(@NotNull audience: Audience, @NotNull other: Audience): @NotNull TagResolver
        = getRelationalGlobalPlaceholders(audience, other)