package me.poma123.globalwarming.api;

import javax.annotation.Nonnull;

/**
 * A very simple API that handles the conversion between
 * {@link TemperatureType} scales.
 *
 * @author poma123
 *
 */
public class Temperature {
    private static double celsiusValue;
    private static TemperatureType tempType = TemperatureType.CELSIUS;

    public Temperature(@Nonnull double celsiusValue) {
        this.celsiusValue = celsiusValue;
    }

    public Temperature(@Nonnull double celsiusValue, @Nonnull TemperatureType tempType) {
        this.celsiusValue = celsiusValue;
        this.tempType = tempType;
    }

    @Nonnull
    public double getCelsiusValue() {
        return celsiusValue;
    }

    @Nonnull
    public double getFahrenheitValue() {
        return celsiusValue * 1.8 + 32;
    }

    @Nonnull
    public double getKelvinValue() {
        return celsiusValue + 273.15;
    }

    @Nonnull
    public double getConvertedValue() {
        switch (tempType) {
            case FAHRENHEIT:
                return getFahrenheitValue();
            case KELVIN:
                return getKelvinValue();
            default:
                return celsiusValue;
        }
    }

    @Nonnull
    public TemperatureType getTemperatureType() {
        return tempType;
    }

    public void setTemperatureType(TemperatureType tempType) {
        this.tempType = tempType;
    }
}
