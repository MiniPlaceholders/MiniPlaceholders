package io.github.miniplaceholders.common.metrics;

import io.github.miniplaceholders.api.MiniPlaceholders;

import java.util.concurrent.Callable;

public final class LoadedExpansionsMetric implements Callable<Integer> {
  @Override
  public Integer call() {
    return MiniPlaceholders.expansionsAvailable().size();
  }
}
