package me.poma123.globalwarming.api;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * A very simple API that handles the conversion between
 * {@link TemperatureType} scales.
 *
 * @author poma123
 *
 */
public class Temperature {
    private double celsiusValue;
    private TemperatureType tempType = TemperatureType.CELSIUS;

    public Temperature(@Nonnull double value) {
        Validate.notNull(value, "The Temperature value should not be null!");

        this.celsiusValue = value;
    }

    public Temperature(@Nonnull double value, @Nonnull TemperatureType type) {
        Validate.notNull(value, "The Temperature value should not be null!");
        Validate.notNull(type, "The TemperatureType should not be null!");

        celsiusValue = value;
        tempType = type;
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

    public void setTemperatureType(@Nonnull TemperatureType type) {
        Validate.notNull(type, "The TemperatureType should not be null!");

        tempType = type;
    }
}
