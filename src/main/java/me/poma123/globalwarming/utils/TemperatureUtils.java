package me.poma123.globalwarming.utils;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.Registry;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.api.TemperatureType;
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
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0#");

    public static String getTemperatureString(Location loc, TemperatureType tempType) {
        Temperature temp = getTemperatureAtLocation(loc);
        double celsiusValue = temp.getCelsiusValue();
        String prefix;

        if (celsiusValue <= 18) {
            prefix = "&b" + COLD;
        }
        else if (celsiusValue <= 24) {
            prefix = "&a" + HOT;
        }
        else if (celsiusValue <= 28) {
            prefix = "&e" + HOT;
        }
        else if (celsiusValue <= 36) {
            prefix = "&6" + HOT;
        }
        else if (celsiusValue <= 45) {
            prefix = "&c" + HOT;
        }
        else {
            prefix = "&4" + HOT;
        }
        temp.setTemperatureType(tempType);

        return prefix + " " + DECIMAL_FORMAT.format(temp.getConvertedValue()) + " &7" + tempType.getSuffix();
    }

   /* public static String getAirQualityString(TemperatureType tempType) {
        Temperature temp = new Temperature(0.1, tempType);
        double celsiusValue = temp.getCelsiusValue();
        String prefix;
        //(celsiusValue > 0 ? "&7+" : "&7-")

        if (celsiusValue <= -1.5 || celsiusValue >= 1.5) {
            prefix = "&c";
        } else if (celsiusValue <= -0.5 || celsiusValue >= 0.5) {
            prefix = "&e";
        } else if (celsiusValue < 0 || celsiusValue > 0) {
            prefix = "&a";
        } else {
            prefix = "&f";
        }

        prefix = (celsiusValue > 0 ? "&7+" : "") + prefix;

        return prefix + DECIMAL_FORMAT.format(temp.getCelsiusValue()) + " &7" + TemperatureType.CELSIUS.getSuffix();
    }*/

    public static Temperature getTemperatureAtLocation(Location loc) {
        World world = loc.getWorld();
        Biome biome = loc.getBlock().getBiome();
        Map<Biome, Double> tempMap = GlobalWarming.getRegistry().getDefaultBiomeTemperatures();
        double celsiusValue = 15;

        if (tempMap.containsKey(biome)) {
            celsiusValue = tempMap.get(biome);
        }

        if (world.getEnvironment() == World.Environment.NORMAL) {
            if (!isDaytime(world)) {
                celsiusValue = celsiusValue - NIGHT_TEMPERATURE_DROP;
            }
            else if (world.hasStorm()) {
                celsiusValue = celsiusValue - STORM_TEMPERATURE_DROP;
            }
        }

        // Multiply by 0.5 for test porpuses only, will be configurable
        celsiusValue = celsiusValue + (PollutionUtils.getPollutionInWorld(world) * Registry.POLLUTION_MULTIPLY);

        return new Temperature(celsiusValue);
    }

    public static boolean isDaytime(World world) {
        long time = world.getTime();
        return (time < 12300 || time > 23850);
    }
}
