package io.github.miniplaceholders.common.command;

import com.google.common.base.Suppliers;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.miniplaceholders.api.types.Platform;
import io.github.miniplaceholders.common.PluginConstants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.william278.desertwell.about.AboutMenu;
import net.william278.desertwell.util.Version;

import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class CommandConstants {
  // Global
  public static final Component TITLE = miniMessage().deserialize("<b><gradient:#4d8bff:#a4ff96>MiniPlaceholders</b>");
  public static final Component HEADER = miniMessage().deserialize(
      """
          <gradient:aqua:white:aqua><st><b>               </st> \
          <title> \
          <st><b>               </st>""", Placeholder.component("title", TITLE));
  public static final Component FOOTER = miniMessage().deserialize(
      "<gradient:aqua:white:aqua><st><b>                                                   ");

  // -- /miniplaceholders --
  public static final Component INFO = AboutMenu.builder()
      .title(TITLE)
      .description(text("MiniMessage Component-based Placeholders API"))
      .credits("Author", AboutMenu.Credit.of("4drian3d").url("https://github.com/4drian3d"))
      .credits("Contributors",
          AboutMenu.Credit.of("Sliman4"),
          AboutMenu.Credit.of("Stampede2011"),
          AboutMenu.Credit.of("powercasgamer"),
          AboutMenu.Credit.of("fabianmakila"),
          AboutMenu.Credit.of("Willsr71")
      )
      .buttons(
          AboutMenu.Link.of("https://miniplaceholders.github.io/docs/category/user-guide").text("Documentation").icon("⛏"),
          AboutMenu.Link.of("https://discord.gg/5NMMzK5mAn").text("Discord").color(TextColor.color(0x7289da)).icon("⭐"),
          AboutMenu.Link.of("https://modrinth.com/plugin/miniplaceholders").text("Downloads").color(TextColor.color(0xff496e)).icon("↓")
      ).version(Version.fromString(PluginConstants.VERSION))
      .build()
      .toComponent();

  // -- /miniplaceholders help --
  public static final Supplier<Component> HELP = Suppliers.memoize(() -> {
    final Platform platform = MiniPlaceholders.platform();
    final String command = platform == Platform.VELOCITY ? "/vminiplaceholders" : "/miniplaceholders";
    return text()
        .append(newline(), HEADER)
        .append(newline(), miniMessage().deserialize(
            "<gradient:#9694ff:#5269ff>Commands:</gradient>")
        )
        .append(newline(), miniMessage().deserialize(
            "<gradient:aqua:#94d1ff>" + command + "</gradient> <aqua>help</aqua>")
            .clickEvent(ClickEvent.runCommand(command + " help"))
        )
        .append(newline(), miniMessage().deserialize(
            """
                <gradient:aqua:#94d1ff>%s</gradient> \
                <aqua>parse</aqua> \
                <#8fadff><player | me | --null></#8fadff> \
                <#99ffb6><message></#99ffb6>""".formatted(command))
            .clickEvent(ClickEvent.suggestCommand(command + " parse "))
        )
        .append(newline(), miniMessage().deserialize(
            """
                <gradient:aqua:#94d1ff>%s</gradient> \
                <aqua>parserel</aqua> \
                <#8fadff><player | me></#8fadff> \
                <#8fadff><relational player | me></#8fadff> \
                <#99ffb6><message></#99ffb6>""".formatted(command))
            .clickEvent(ClickEvent.suggestCommand(command + " parserel "))
        )
        .append(newline(), miniMessage().deserialize(
            "<gradient:aqua:#94d1ff>" + command + "</gradient> <aqua>expansions</aqua>")
            .clickEvent(ClickEvent.runCommand(command + " help"))
        )
        .append(newline(), FOOTER)
        .build();
  });

  // -- /miniplaceholders expansions --
  public static final Component EXPANSIONS_TITLE = miniMessage().deserialize(
      """
          <br><gradient:aqua:white:aqua><st><b>         </st> \
          <title> Expansions <st><b>         \
          </gradient><br>""", Placeholder.component("title", TITLE)
  );

  public static final Supplier<Component> NON_INSTALLED_EXPANSIONS = Suppliers.memoize(() -> miniMessage().deserialize(
      """
          <aqua>It seems that you don't have any <#a1ff4a>Expansion</#a1ff4a> installed yet,
          You can find the <#a1ff4a>Expansion</#a1ff4a> you want here <br>\
          <blue><click:open_url:"https://miniplaceholders.github.io/docs/user-guide/Placeholders">\
          <hover:show_text:"<gold>Search Expansions">[MiniPlaceholders Expansions]</blue>""")
  );
}
