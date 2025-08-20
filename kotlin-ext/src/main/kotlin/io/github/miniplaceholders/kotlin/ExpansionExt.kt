package io.github.miniplaceholders.kotlin

import io.github.miniplaceholders.api.Expansion
import io.github.miniplaceholders.api.resolver.AudienceTagResolver
import io.github.miniplaceholders.api.resolver.GlobalTagResolver
import io.github.miniplaceholders.api.resolver.RelationalTagResolver
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.Tag

/**
 * Create a new Expansion
 *
 * @param name the expansion name
 * @param builder a builder-like function to create an Expansion from a builder
 * @return a new expansion
 * @since 1.4.0
 * @see Expansion.builder
 * @see Expansion.Builder.build
 * */
public fun expansion(name: String, builder: Expansion.Builder.() -> Expansion.Builder): Expansion =
    builder(Expansion.builder(name)).build()

public fun Expansion.Builder.global(placeholderName: String, resolver: GlobalTagResolver): Expansion.Builder =
    this.globalPlaceholder(placeholderName, resolver)

public fun Expansion.Builder.global(placeholderName: String, tag: Tag): Expansion.Builder =
    this.globalPlaceholder(placeholderName, tag)

public inline fun <reified A : Audience> Expansion.Builder.audience(
    placeholderName: String,
    resolver: AudienceTagResolver<A>
): Expansion.Builder =
    this.audiencePlaceholder(A::class.java, placeholderName, resolver)

public inline fun <reified A : Audience> Expansion.Builder.relational(
    placeholderName: String,
    resolver: RelationalTagResolver<A>
): Expansion.Builder =
    this.relationalPlaceholder(A::class.java, placeholderName, resolver)
