package me.poma123.globalwarming;

public class Temperature {
    private static double celsiusValue;

    public Temperature(double celsiusValue) {
        this.celsiusValue = celsiusValue;
    }

    public double getFahrenheitValue() {
        return celsiusValue*1.8+32;
    }

    public double getCelsiusValue() {
        return celsiusValue;
    }
}
