package io.github.miniplaceholders.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/** Checks required for the correct operation of the expansions*/
public final class Conditions {
    private Conditions() {}

    /**
     * Checks if a string is null or empty or consists of whitespace characters only.
     *
     * @param string the string
     * @param name the name supplier
     * @return the string if it is not null or empty
     * @since 1.0.0
     * @throws IllegalStateException if the provided string is empty
     */
    public static String nonNullOrEmptyString(@Nullable String string, @NotNull Supplier<@NotNull String> name){
        if (string == null)
            throw new NullPointerException(name.get() + " cannot be null");
        if (string.isBlank())
            throw new IllegalStateException(name.get() + "cannot be an empty string");
        return string;
    }

    /**
     * Checks if a string is null or empty or consists of whitespace characters only.
     *
     * @param string the string
     * @param name the name
     * @return the string if it is not null or empty
     * @since 2.2.0
     * @throws IllegalStateException if the provided string is empty
     */
    public static String nonNullOrEmptyString(@Nullable String string, @NotNull String name){
        if (string == null)
            throw new NullPointerException(name + " cannot be null");
        if (string.isBlank())
            throw new IllegalStateException(name + "cannot be an empty string");
        return string;
    }

    /**
     * Check if a string is empty or consists of whitespace characters only.
     *
     * @param string the string
     * @param reason the reason supplier
     * @return the string if it not empty
     * @since 1.0.0
     * @throws IllegalStateException if the provided string is empty
     */
    public static String nonEmptyString(@NotNull String string, @NotNull Supplier<@NotNull String> reason){
        if (string.isBlank()) throw new IllegalStateException(reason.get());
        return string;
    }

    /**
     * Check if a string is empty or consists of whitespace characters only.
     *
     * @param string the string
     * @param reason the reason
     * @return the string if it not empty
     * @since 2.2.0
     * @throws IllegalStateException if the provided string is empty
     */
    public static String nonEmptyString(@NotNull String string, @NotNull String reason){
        if (string.isBlank()) throw new IllegalStateException(reason);
        return string;
    }
    
}
