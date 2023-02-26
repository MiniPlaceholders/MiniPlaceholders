package me.dreamerzero.miniplaceholders.kotlin

import me.dreamerzero.miniplaceholders.api.Expansion

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
public fun expansion(name: String, builder: Expansion.Builder.() -> Expansion.Builder): Expansion
    = builder(Expansion.builder(name)).build()
