package io.github.miniplaceholders.common.loader;

import io.github.miniplaceholders.api.provider.ExpansionProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class ExpansionProviderLoader {

  public static List<ExpansionProvider> loadProvidersFromFolder(final Path folferPath) throws Exception {
    if (Files.notExists(folferPath)) {
      try {
        Files.createDirectories(folferPath);
      } catch (IOException e) {
        throw new RuntimeException("Unable to create expansions directory", e);
      }
      return List.of();
    }
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(folferPath,
            filtered -> Files.isRegularFile(filtered) && filtered.toString().endsWith(".jar"))) {
      final List<ExpansionProvider> providerList = new ArrayList<>();
      for (final Path possibleProviderPath : stream) {
        loadPossibleProviderFromPath(possibleProviderPath, providerList);
      }
      return providerList;
    }
  }

  private static void loadPossibleProviderFromPath(
          final Path filePath,
          final List<ExpansionProvider> providers
  ) throws Exception {
    String mainClassName = null;
    try (final JarInputStream jarStream = new JarInputStream(Files.newInputStream(filePath))) {
      JarEntry actualEntry;
      while ((actualEntry = jarStream.getNextJarEntry()) != null) {
        final String entryName = actualEntry.getName();
        if (!Objects.equals(entryName, "expansion-provider.properties")) {
          continue;
        }
        try (final Reader reader = new InputStreamReader(jarStream)) {
          Properties properties = new Properties();
          properties.load(reader);
          mainClassName = properties.getProperty("provider-class");
          break;
        } catch (IOException e) {
          throw new IOException(
                  "Unable to read expansion_provider.properties from " + filePath + "file",
                  e);
        }
      }
    }

    if (mainClassName == null) {
      return;
    }

    loadFoundProvider(mainClassName, providers, filePath);
  }

  private static void loadFoundProvider(
          final String providerClass,
          final List<ExpansionProvider> providers,
          final Path providerPath
  ) throws Exception {
    //noinspection resource
    final URLClassLoader classLoader = new URLClassLoader(new URL[] {
            providerPath.toUri().toURL(),
    }, ExpansionProviderLoader.class.getClassLoader());
    Class<?> mainClass;
    try {
      mainClass = classLoader.loadClass(providerClass);
    } catch (ClassNotFoundException e) {
      throw new ClassNotFoundException("Unable to load main class from file " + providerPath, e);
    }
    if (!ExpansionProvider.class.isAssignableFrom(mainClass) || mainClass.isInterface()) {
      return;
    }
    try {
      providers.add((ExpansionProvider) mainClass.getConstructors()[0].newInstance());
    } catch (Exception e) {
      throw new RuntimeException("Unable to create expansion provider from file " + providerPath, e);
    }
  }
}
