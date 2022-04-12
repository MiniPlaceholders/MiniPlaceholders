package me.dreamerzero.miniplaceholders.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Components {
    private Components(){}
    /**
     * "True" Component
     * @since 1.1.0
     */
    public static final TextComponent TRUE_COMPONENT = Component.text("true", NamedTextColor.GREEN);
    /**
     * False Component
     * @since 1.1.0
     */
    public static final TextComponent FALSE_COMPONENT = Component.text("false", NamedTextColor.RED);

    /**
     * "Yes" Component
     * @since 1.1.0
     */
    public static final TextComponent YES_COMPONENT = Component.text("yes", NamedTextColor.GREEN);
    /**
     * "No" Component
     * @since 1.1.0
     */
    public static final TextComponent NO_COMPONENT = Component.text("no", NamedTextColor.RED);
}
