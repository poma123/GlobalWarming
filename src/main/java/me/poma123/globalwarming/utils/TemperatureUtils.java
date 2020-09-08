package me.poma123.globalwarming.utils;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.objects.Temperature;
import me.poma123.globalwarming.objects.TemperatureType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.text.DecimalFormat;
import java.util.Map;

public class TemperatureUtils {
    public static final String HOT = "☀";
    public static final String COLD = "❄";
    public static final int NIGHT_TEMPERATURE_DROP = 10;
    public static final int STORM_TEMPERATURE_DROP = 5;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0#");

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

        return prefix + " " + DECIMAL_FORMAT.format(temp.getConvertedValue()) + " &7" + tempType.getSuffix();
    }

    public static Temperature getTemperatureAtLocation(Location loc) {
        World w = loc.getWorld();
        Biome b = loc.getBlock().getBiome();
        Map<Biome, Double> tempMap = GlobalWarming.getRegistry().getDefaultBiomeTemperatures();
        double celsiusValue = 15;

        if (tempMap.containsKey(b)) {
            celsiusValue = tempMap.get(b);
        }

        if (!isDaytime(w)) {
            celsiusValue = celsiusValue - NIGHT_TEMPERATURE_DROP;
        } else if (w.hasStorm()) {
            celsiusValue = celsiusValue - STORM_TEMPERATURE_DROP;
        }

        return new Temperature(celsiusValue);
    }

    public static boolean isDaytime(World world) {
        long time = world.getTime();
        return (time < 12300 || time > 23850);
    }
}
