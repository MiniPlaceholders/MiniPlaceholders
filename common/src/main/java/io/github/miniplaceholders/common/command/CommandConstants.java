package io.github.miniplaceholders.common.command;

import io.github.miniplaceholders.common.PluginConstants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.william278.desertwell.about.AboutMenu;
import net.william278.desertwell.util.Version;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class CommandConstants {
  public static final Component TITLE = miniMessage().deserialize("<gradient:#4d8bff:#a4ff96>MiniPlaceholders</gradient>");
  public static final Component HEADER = miniMessage().deserialize(
      "<gradient:aqua:white:aqua><st><b>             </st> <title> <st><b>             </st>", Placeholder.component("title", TITLE));
  public static final Component FOOTER = miniMessage().deserialize(
      "<gradient:aqua:white:aqua><st><b>                                                 ");

  public static final Component INFO = AboutMenu.builder()
      .title(TITLE)
      .description(text("MiniMessage Component-based Placeholders for Paper, Folia, Fabric, Velocity and Sponge platforms"))
      .credits("Author", AboutMenu.Credit.of("4drian3d").url("https://github.com/4drian3d"))
      .credits("Contributors",
          AboutMenu.Credit.of("Sliman4"),
          AboutMenu.Credit.of("Stampede2011"),
          AboutMenu.Credit.of("powercasgamer")
      )
      .buttons(
          AboutMenu.Link.of("https://github.com/MiniPlaceholders/MiniPlaceholders/wiki/User-Getting-Started").text("Documentation").icon("⛏"),
          AboutMenu.Link.of("https://discord.gg/5NMMzK5mAn").text("Discord").color(TextColor.color(0x7289da)).icon("⭐"),
          AboutMenu.Link.of("https://modrinth.com/plugin/miniplaceholders").text("Downloads").color(TextColor.color(0xff496e)).icon("↓")
      ).version(Version.fromString(PluginConstants.VERSION))
      .build()
      .toComponent();
}
