package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.types.Platform;
import net.kyori.examination.Examinable;
import org.jspecify.annotations.NullMarked;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public sealed interface LoadRequirement {

  static LoadRequirement none() {
    return NoneLoadRequirement.INSTANCE;
  }

  static AvailableComplementRequirement requiredComplement(final String name, String... aliases) {
    requireNonNull(name);
    return new AvailableComplementRequirement(name, aliases);
  }

  static MultiLoadRequirement allOf(LoadRequirement... requirements) {
    requireNonNull(requirements);
    assert requirements.length != 0;
    return new MultiLoadRequirement(requirements);
  }

  static PlatformRequirement platform(Platform... platforms) {
    requireNonNull(platforms);
    assert platforms.length != 0;

    return new PlatformRequirement(platforms);
  }

  enum NoneLoadRequirement implements LoadRequirement {
    INSTANCE
  }

  @NullMarked
  record AvailableComplementRequirement(String name, String... platformAliases) implements LoadRequirement {

    public boolean shouldLoad(Predicate<String> platformComplementTester) {
      if (platformComplementTester.test(name)) {
        return true;
      }
      for (String alias : platformAliases) {
        if (platformComplementTester.test(alias)) {
          return true;
        }
      }
      return false;
    }

      @Override
      public String toString() {
          return "AvailableComplementRequirement{" +
                  "complement='" + name + '\'' +
                  '}';
      }
  }

  @NullMarked
  record MultiLoadRequirement(LoadRequirement... requirements) implements LoadRequirement {
  }

  @NullMarked
  record PlatformRequirement(Platform... platforms) implements LoadRequirement, Examinable {
      @Override
      public String toString() {
          final String platforms = Stream.of(this.platforms)
                  .map(Platform::name)
                  .collect(Collectors.joining(", "));
          return "PlatformRequirement{" +
                  "platforms=" + platforms +
                  '}';
      }
  }
}
