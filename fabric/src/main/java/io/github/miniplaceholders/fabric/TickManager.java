package io.github.miniplaceholders.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class TickManager {
    private static final int TICK_DURATION = 20;

    private static final BigDecimal TPS_BASE = new BigDecimal(1E9).multiply(new BigDecimal(TICK_DURATION));

    private final RollingAverage tps1m = new RollingAverage(60);
    private final RollingAverage tps5m = new RollingAverage(60 * 5);
    private final RollingAverage tps15m = new RollingAverage(60 * 15);
    private long lastStartTick = 0;
    private long lastEndTick = 0;

    TickManager() {
        ServerTickEvents.START_SERVER_TICK.register(server -> this.lastStartTick = System.nanoTime());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % TICK_DURATION == 0) {
                final long diff = lastStartTick - lastEndTick;
                final BigDecimal currentTps = TPS_BASE.divide(new BigDecimal(diff), 30, RoundingMode.HALF_UP);
                this.tps1m.add(currentTps, diff);
                this.tps5m.add(currentTps, diff);
                this.tps15m.add(currentTps, diff);
                this.lastEndTick = System.nanoTime();
            }
        });
    }

    public RollingAverage getTps1m() {
        return tps1m;
    }

    public RollingAverage getTps5m() {
        return tps5m;
    }

    public RollingAverage getTps15m() {
        return tps15m;
    }
}
