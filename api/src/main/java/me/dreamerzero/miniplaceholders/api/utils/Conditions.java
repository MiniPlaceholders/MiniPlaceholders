package me.dreamerzero.miniplaceholders.api.utils;

import java.util.function.Supplier;

/** Checks required for the correct operation of the expansions*/
public final class Conditions {
    private Conditions(){}

    /**
     * Checks if a string is null or empty or consists of whitespace characters only
     * @param string the string
     * @param name the name supplier
     * @return the string if it is not null or empty
     * @since 1.0.0
     */
    public static String nonNullOrEmptyString(String string, Supplier<String> name){
        if(string == null)
            throw new NullPointerException("the " + name.get() + " cannot be null");
        if(string.isBlank())
            throw new IllegalStateException("the " + name.get() + "cannot be an empty string");
        return string;
    }

    /**
     * Check if a string is empty or consists of whitespace characters only
     * @param string the string
     * @param reason the reason supplier
     * @return the string if it not empty
     * @since 1.0.0
     */
    public static String nonEmptyString(String string, Supplier<String> reason){
        if(string.isBlank())
            throw new IllegalStateException(reason.get());
        return string;
    }
    
}
