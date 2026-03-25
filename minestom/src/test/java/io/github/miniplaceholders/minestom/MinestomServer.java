package io.github.miniplaceholders.minestom;

import io.github.miniplaceholders.minestom.listener.AsyncPlayerConfigurationListener;
import net.kyori.adventure.util.TriState;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.lan.OpenToLAN;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class MinestomServer {

  private static final Path MINIPLACEHOLDERS_TEST_FOLDER = Path.of("minestom", "test", "miniplaceholders");
  private static final Path TEST_EXPANSION_FOLDER = Path.of("example-expansion-provider", "build", "libs");

  static void main() throws IOException {
    final MinecraftServer server = MinecraftServer.init(new Auth.Online());
    MinecraftServer.getGlobalEventHandler().addListener(new AsyncPlayerConfigurationListener());
    MinestomServer.copyTestExpansion();

    MinestomMiniPlaceholders.initialize(
        MINIPLACEHOLDERS_TEST_FOLDER,
        (_, _) -> TriState.TRUE
    );

    OpenToLAN.open();
    server.start("0.0.0.0", 25565);

    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(Duration.ofMinutes(2));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.exit(0);
    });
  }

  private static void copyTestExpansion() throws IOException {
    if (!Files.exists(TEST_EXPANSION_FOLDER)) {
      return;
    }

    final Path expansionsFolder = MINIPLACEHOLDERS_TEST_FOLDER.resolve("expansions");
    // Delete previous expansions files
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(
        expansionsFolder,
        file -> file.getFileName().toString().endsWith(".jar"))
    ) {
      stream.forEach(file -> {
        try {
          Files.delete(file);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }

    // Copy latest example expansion file
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(
        TEST_EXPANSION_FOLDER,
        file -> file.getFileName()
            .toString().startsWith("miniplaceholders-example-expansion-provider"))
    ) {
      stream.forEach(file -> {
        if (!Files.exists(expansionsFolder)) {
          try {
            Files.createDirectories(expansionsFolder);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        try {
          Files.copy(file, expansionsFolder.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (IOException e) {
      System.out.println("An error occurred while loading expansion provider" + e);
    }
  }

}
