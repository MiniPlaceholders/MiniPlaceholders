package io.github.miniplaceholders.kotlin

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * Check if this TagResolver is an instance of TagResolver.empty(),
 * that is, it will always return null
 * @return true if the TagResolver is the same as TagResolver.empty(), else false
 * @since 1.4.0
 * @see TagResolver.empty
 */
public fun TagResolver.isEmpty(): Boolean = this == TagResolver.empty()

/**
 * Checks if this TagResolver is not a TagResolver.empty()
 * @return false if the TagResolver is the same as TagResolver.empty(), else true
 * @since 1.4.0
 * @see TagResolver.empty
 */
public fun TagResolver.isNotEmpty(): Boolean = this != TagResolver.empty()

/**
 * Applies a TagResolver to a TagResolver.Builder in case it is not a TagResolver.empty()
 * @param resolver the resolver to check
 * @since 1.4.0
 * @see TagResolver.isNotEmpty
 */
public fun TagResolver.Builder.applyIfNotEmpty(resolver: TagResolver) {
    if (resolver.isNotEmpty()) {
        resolver(resolver)
    }
}
