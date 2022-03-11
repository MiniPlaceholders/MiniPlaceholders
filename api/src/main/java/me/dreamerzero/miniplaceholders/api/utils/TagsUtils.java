package me.dreamerzero.miniplaceholders.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

/**Tags utils */
public final class TagsUtils {
    private TagsUtils(){}

    /**Empty Tag */
    public static final Tag EMPTY_TAG = Tag.selfClosingInserting(Component.empty());
}
