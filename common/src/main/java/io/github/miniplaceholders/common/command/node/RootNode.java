package io.github.miniplaceholders.common.command.node;

import io.github.miniplaceholders.common.PluginConstants;
import io.github.miniplaceholders.common.command.PermissionTester;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.TriState;
import net.william278.desertwell.about.AboutMenu;
import net.william278.desertwell.util.Version;
import org.incendo.cloud.Command;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;

import static io.github.miniplaceholders.common.command.PlaceholdersCommand.TITLE;
import static net.kyori.adventure.text.Component.text;

public record RootNode<S extends Audience>(PermissionTester<S> hasPermission) implements Node<S> {
  private static final Component INFO = AboutMenu.builder()
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

  @Override
  public Command.Builder<S> apply(Command.Builder<S> rootBuilder) {
    return rootBuilder
            .permission(src -> {
              if (hasPermission != null) {
                return PermissionResult.of(hasPermission.hasPermission(src, "miniplaceholders.command"), Permission.permission("miniplaceholders.command"));
              }
              return PermissionResult.of(src.get(PermissionChecker.POINTER)
                              .map(checker -> checker.value("miniplaceholders.command"))
                              .orElse(TriState.FALSE) != TriState.FALSE,
                      Permission.permission("miniplaceholders.command")
              );
            })
            .handler(handler -> handler.sender().sendMessage(INFO));
  }
}
