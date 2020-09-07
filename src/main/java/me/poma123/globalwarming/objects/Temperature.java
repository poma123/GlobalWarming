package me.poma123.globalwarming.objects;

import javax.annotation.Nonnull;

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

    public double getCelsiusValue() {
        return celsiusValue;
    }

    public double getFahrenheitValue() {
        return celsiusValue*1.8+32;
    }

    public double getKelvinValue() {
        return celsiusValue + 273.15;
    }

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

    public TemperatureType getTemperatureType() {
        return tempType;
    }
}
