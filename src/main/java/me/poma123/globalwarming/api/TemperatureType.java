package me.poma123.globalwarming.api;

import javax.annotation.Nonnull;

public enum TemperatureType {

    CELSIUS("Celsius", "°C"),
    FAHRENHEIT("Fahrenheit", "°F"),
    KELVIN("Kelvin", "K");

    private final String name;
    private final String suffix;

    private TemperatureType(@Nonnull String name, @Nonnull String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
