package me.poma123.globalwarming.utils;

import me.poma123.globalwarming.Temperature;
import me.poma123.globalwarming.TemperatureType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;

public class TemperatureUtils {

    public static final String COLD = "❄";
    public static final String HOT = "☀";

    private TemperatureUtils() {
    }

    public static String getTemperatureString(Block b, TemperatureType temperatureType) {
        Temperature temp = new Temperature(10);
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

        return prefix + " " + (temperatureType == TemperatureType.CELSIUS ? celsiusValue + " &7°C" : temp.getFahrenheitValue() + " &7°F");
    }
}
