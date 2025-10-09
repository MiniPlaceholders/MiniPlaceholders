import org.jspecify.annotations.NullMarked;

/**
 * MiniPlaceholders API Module
 */
@NullMarked
open module io.github.miniplaceholders.api {
  requires io.github.miniplaceholders.connect;

  requires net.kyori.adventure;
  requires net.kyori.adventure.text.minimessage;
  requires net.kyori.adventure.text.serializer.legacy;
  requires net.kyori.examination.api;

  requires static org.jetbrains.annotations;
  requires static org.jspecify;

  exports io.github.miniplaceholders.api;
  exports io.github.miniplaceholders.api.types;
  exports io.github.miniplaceholders.api.utils;
  exports io.github.miniplaceholders.api.resolver;
  exports io.github.miniplaceholders.api.placeholder;
  exports io.github.miniplaceholders.api.provider;
}
