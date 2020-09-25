package me.poma123.globalwarming.utils;

import java.util.Map;

import me.mrCookieSlime.Slimefun.cscorelib2.math.DoubleHandler;
import me.poma123.globalwarming.api.PollutionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.api.TemperatureType;

import javax.annotation.Nonnull;

public class TemperatureUtils {

    public static final String HOT = "☀";
    public static final String COLD = "❄";
    public static final int NIGHT_TEMPERATURE_DROP = 10;
    public static final int STORM_TEMPERATURE_DROP = 5;

    public static String getTemperatureString(@Nonnull Location loc, @Nonnull TemperatureType tempType) {
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

        return prefix + " " + DoubleHandler.fixDouble(temp.getConvertedValue()) + " &7" + tempType.getSuffix();
    }

      public static String getAirQualityString(@Nonnull Location loc, @Nonnull TemperatureType tempType) {
        Temperature temp = getTemperatureAtLocation(loc);

        double currentValue = temp.getCelsiusValue();
        double defaultValue = getDefaultBiomeTemperatureAtLocation(loc).getCelsiusValue();
        double celsiusDifference = getDifference(currentValue, defaultValue, TemperatureType.CELSIUS);
        String prefix;

        if (celsiusDifference <= -1.5 || celsiusDifference >= 1.5) {
            prefix = "&c";
        } else if (celsiusDifference <= -0.5 || celsiusDifference >= 0.5) {
            prefix = "&e";
        } else if (celsiusDifference < 0 || celsiusDifference > 0) {
            prefix = "&a";
        } else {
            prefix = "&f";
        }

        double difference = getDifference(currentValue, defaultValue, tempType);
        
        prefix = prefix + (difference > 0 ? "+" : "");

        return "&7Climate change: " + prefix + DoubleHandler.fixDouble(difference) + " &7" + tempType.getSuffix();
    }

    public static Temperature getTemperatureAtLocation(@Nonnull Location loc) {
        World world = loc.getWorld();
        double celsiusValue = getDefaultBiomeTemperatureAtLocation(loc).getCelsiusValue();
        
        celsiusValue = celsiusValue + (PollutionManager.getPollutionInWorld(world) * GlobalWarming.getRegistry().getPollutionMultiply());

        return new Temperature(celsiusValue);
    }

    public static Temperature getDefaultBiomeTemperatureAtLocation(@Nonnull Location loc) {
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

        return new Temperature(celsiusValue);
    }

    public static double getDifference(@Nonnull double currentValue, @Nonnull double defaultValue, @Nonnull TemperatureType type) {

        double convertedCurrent = new Temperature(currentValue, type).getConvertedValue();
        double convertedDefault = new Temperature(defaultValue, type).getConvertedValue();

        double difference = Math.abs(convertedCurrent - convertedDefault);

        if (convertedCurrent < convertedDefault) {
            difference = difference*-1;
        }

        return difference;
    }

    public static boolean isDaytime(@Nonnull World world) {
        long time = world.getTime();
        return (time < 12300 || time > 23850);
    }
}
