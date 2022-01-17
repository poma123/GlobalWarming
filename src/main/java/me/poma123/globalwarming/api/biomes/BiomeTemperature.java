package me.poma123.globalwarming.api.biomes;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

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
        Validate.notNull(temperature, "The temperature value should not be null!");
        Validate.notNull(maxTemperatureDropAtNight, "The maxTemperatureDropAtNight value should not be null!");

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
