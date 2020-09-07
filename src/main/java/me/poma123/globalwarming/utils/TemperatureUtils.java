package me.poma123.globalwarming.utils;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.objects.Temperature;
import me.poma123.globalwarming.objects.TemperatureType;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.Map;

public class TemperatureUtils {
    public static final String COLD = "❄";
    public static final String HOT = "☀";

    public static String getTemperatureString(Location loc, TemperatureType tempType) {
        Temperature temp = getTemperatureAtLocation(loc);
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

        temp.setTemperatureType(tempType);

        return prefix + " " + temp.getConvertedValue() + " &7" + temp.getTemperatureType().getSuffix();
    }

    public static Temperature getTemperatureAtLocation(Location loc) {
        Biome b = loc.getBlock().getBiome();
        Map<Biome, Double> tempMap = GlobalWarming.getRegistry().getDefaultBiomeTemperatures();
        double celsiusValue = 15;

        if (tempMap.containsKey(b)) {
            celsiusValue = tempMap.get(b);
        }

        return new Temperature(celsiusValue);
    }
}
