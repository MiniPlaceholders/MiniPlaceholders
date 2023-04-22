package io.github.miniplaceholders.fabric;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Based on the MIT licensed Paper-Server patch "Further improve server tick loop".
 *
 * @author Daniel Ennis/Aikar
 */
final class RollingAverage {
    private static final DecimalFormat TPS_FORMAT = new DecimalFormat("###.##");
    static { TPS_FORMAT.setRoundingMode(RoundingMode.HALF_UP); }
    private static final int TPS = 20;
    private static final long SEC_IN_NANO = 1000000000;
    private final int size;
    private final BigDecimal[] samples;
    private final long[] times;
    private long time;
    private BigDecimal total;
    private int index = 0;

    RollingAverage(final int size) {
        this.size = size;
        this.samples = new BigDecimal[size];
        this.times = new long[size];
        this.time = size * SEC_IN_NANO;
        this.total = dec(TPS).multiply(dec(SEC_IN_NANO)).multiply(dec(size));
        for (int i = 0; i < size; i++) {
            this.samples[i] = dec(TPS);
            this.times[i] = SEC_IN_NANO;
        }
    }

    private static BigDecimal dec(final long t) {
        return new BigDecimal(t);
    }

    void add(final BigDecimal x, final long t) {
        this.time -= this.times[this.index];
        this.total = this.total.subtract(this.samples[this.index].multiply(dec(this.times[this.index])));
        this.samples[this.index] = x;
        this.times[this.index] = t;
        this.time += t;
        this.total = this.total.add(x.multiply(dec(t)));
        if (++this.index == this.size) {
            this.index = 0;
        }
    }

    double average() {
        return this.total.divide(dec(this.time), 30, RoundingMode.HALF_UP).doubleValue();
    }

    String formattedAverage() {
        return TPS_FORMAT.format(average());
    }
}