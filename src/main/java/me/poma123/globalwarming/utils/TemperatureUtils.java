package me.poma123.globalwarming.utils;

import me.poma123.globalwarming.objects.Temperature;
import me.poma123.globalwarming.objects.TemperatureType;
import org.bukkit.block.Block;

public class TemperatureUtils {

    public static final String COLD = "❄";
    public static final String HOT = "☀";

    private TemperatureUtils() {
    }

    public static String getTemperatureString(Block b, TemperatureType tempType) {
        Temperature temp = new Temperature(10, tempType);
        double celsiusValue = temp.getCelsiusValue();
        String prefix;

        if (celsiusValue <= 18) {
            prefix = "&b" + COLD;
        } else if (celsiusValue <= 24) {
            prefix = "&e" + HOT;
        } else if (celsiusValue <= 36) {
            prefix = "&6" + HOT;
        } else {
            prefix = "&c" + HOT;
        }

        return prefix + " " + temp.getConvertedValue() + " &7" + temp.getTemperatureType().getSuffix();
    }
}
