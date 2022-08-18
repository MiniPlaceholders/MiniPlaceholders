module me.dreamerzero.miniplaceholders.api {
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires net.kyori.adventure.text.minimessage;
    requires static org.jetbrains.annotations;
    requires me.dreamerzero.miniplaceholders.connect;

    exports me.dreamerzero.miniplaceholders.api;
    exports me.dreamerzero.miniplaceholders.api.enums;
    exports me.dreamerzero.miniplaceholders.api.utils;
    exports me.dreamerzero.miniplaceholders.api.placeholder;
}
