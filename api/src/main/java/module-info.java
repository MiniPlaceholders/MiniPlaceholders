/** MiniPlaceholdes API Module */
open module me.dreamerzero.miniplaceholders.api {
    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.examination.api;
    requires static org.jetbrains.annotations;
    requires me.dreamerzero.miniplaceholders.connect;
    requires net.kyori.adventure.text.serializer.legacy;

    exports me.dreamerzero.miniplaceholders.api;
    exports me.dreamerzero.miniplaceholders.api.enums;
    exports me.dreamerzero.miniplaceholders.api.utils;
    exports me.dreamerzero.miniplaceholders.api.placeholder;
}
