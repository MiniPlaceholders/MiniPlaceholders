/** MiniPlaceholders API Module */
open module io.github.miniplaceholders.api {
    requires io.github.miniplaceholders.connect;

    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure.text.serializer.legacy;
    requires net.kyori.examination.api;

    requires static org.jetbrains.annotations;

    exports io.github.miniplaceholders.api;
    exports io.github.miniplaceholders.api.enums;
    exports io.github.miniplaceholders.api.utils;
    exports io.github.miniplaceholders.api.placeholder;
}
