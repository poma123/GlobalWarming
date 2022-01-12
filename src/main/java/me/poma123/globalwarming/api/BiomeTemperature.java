package me.poma123.globalwarming.api;

import javax.annotation.Nonnull;

/**
 * This data type holds biome temperature data for our
 * {@link io.github.thebusybiscuit.slimefun4.utils.biomes.BiomeMap} file
 *
 * @author poma123
 *
 */
public class BiomeTemperature {

    private final double temperature;
    private final double maxTemperatureDropAtNight;

    @Nonnull
    public BiomeTemperature(@Nonnull double temperature, @Nonnull double maxTemperatureDropAtNight) {
        this.temperature = temperature;
        this.maxTemperatureDropAtNight = maxTemperatureDropAtNight;
    }

    @Nonnull
    public double getTemperature() {
        return temperature;
    }

    @Nonnull
    public double getMaxTemperatureDropAtNight() {
        return maxTemperatureDropAtNight;
    }
}
