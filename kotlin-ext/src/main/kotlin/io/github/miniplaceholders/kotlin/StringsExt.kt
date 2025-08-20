package io.github.miniplaceholders.kotlin

import io.github.miniplaceholders.api.utils.LegacyStrings
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

public fun String.hasLegacyFormat() : Boolean = LegacyStrings.hasLegacyFormat(this)

public fun String.parsePossibleLegacy(resolver: TagResolver = TagResolver.empty()) : Component =
    LegacyStrings.parsePossibleLegacy(this, resolver)

public fun String.parsePossibleLegacy(context: Context) : Component =
    LegacyStrings.parsePossibleLegacy(this, context)
