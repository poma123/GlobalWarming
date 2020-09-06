package me.poma123.globalwarming;

import javax.annotation.Nonnull;

public enum TemperatureType {

    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit");

    private final String suffix;

    private TemperatureType(@Nonnull String suffix) {
        this.suffix = suffix;
    }

    public String toString() {
        return this.suffix;
    }
}
