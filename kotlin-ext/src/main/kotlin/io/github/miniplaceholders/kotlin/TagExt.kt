package io.github.miniplaceholders.kotlin

import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.tag.Tag

/**
 * Converts a [ComponentLike] to a [Tag] of type selfClosingInserting
 *
 * @see Tag.selfClosingInserting
 */
public fun ComponentLike.asClosingTag(): Tag = Tag.selfClosingInserting(this)

/**
 * Converts a [ComponentLike] to a [Tag] of type inserting
 *
 * @see Tag.inserting
 */
public fun ComponentLike.asInsertingTag(): Tag = Tag.inserting(this)

/**
 * Converts a [ComponentLike] to a [Tag] of type preProcessParsed
 *
 * @see Tag.preProcessParsed
 */
public fun String.asPreProcessTag(): Tag = Tag.preProcessParsed(this)
