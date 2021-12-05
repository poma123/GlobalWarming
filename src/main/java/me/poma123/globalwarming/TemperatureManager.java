package me.poma123.globalwarming;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import me.poma123.globalwarming.api.PollutionManager;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.api.TemperatureType;

/**
 * Handles the temperature calculations in different {@link Biome} instances
 * based on default biome temperature, pollution, weather and time.
 *
 * @author poma123
 *
 */
public class TemperatureManager {

    public static final String HOT = "☀";
    public static final String COLD = "❄";

    private static final Set<Map.Entry<Biome, Double>> tempSet = GlobalWarmingPlugin.getRegistry().getDefaultBiomeTemperatures().entrySet();
    private final Map<Biome, Double> nightDropMap = GlobalWarmingPlugin.getRegistry().getMaxTemperatureDropsAtNight();
    private final Map<String, EnumMap<Biome, Double>> worldTemperatureChangeFactorMap = new HashMap<>();

    protected void runCalculationTask(long delay, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(GlobalWarmingPlugin.getInstance(), () -> {

            for (String w : GlobalWarmingPlugin.getRegistry().getEnabledWorlds()) {
                if (GlobalWarmingPlugin.getRegistry().isWorldEnabled(w)) {
                    World world = Bukkit.getWorld(w);

                    if (world != null && !world.getPlayers().isEmpty()) {
                        EnumMap<Biome, Double> map = new EnumMap<>(Biome.class);
                        boolean isNormalEnvironment = world.getEnvironment() == World.Environment.NORMAL;

                        for (Map.Entry<Biome, Double> entry : tempSet) {
                            Biome biome = entry.getKey();
                            Temperature defaultTemperature = new Temperature(entry.getValue());
                            Temperature newTemp = isNormalEnvironment ? addTemperatureChangeFactors(world, biome, defaultTemperature) : defaultTemperature;

                            map.put(biome, newTemp.getCelsiusValue());
                        }
                        worldTemperatureChangeFactorMap.put(w, map);
                    }
                }
            }
        }, delay, interval);
    }

    public Temperature getTemperatureAtLocation(@Nonnull Location loc) {
        World world = loc.getWorld();
        Biome biome = loc.getBlock().getBiome();

        EnumMap<Biome, Double> map = worldTemperatureChangeFactorMap.get(world.getName());

        if (map == null) {
            return new Temperature(0);
        }

        return new Temperature(map.get(biome));
    }

    public String getTemperatureString(@Nonnull Location loc, @Nonnull TemperatureType tempType) {
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(loc.getWorld().getName())) {
            return "&cNon-functional in this world.";
        }

        Temperature temp = getTemperatureAtLocation(loc);

        if (temp == null) {
            return "&7Measuring...";
        }

        double celsiusValue = temp.getCelsiusValue();
        String prefix;

        if (celsiusValue <= 18) {
            prefix = "&b" + COLD;
        } else if (celsiusValue <= 24) {
            prefix = "&a" + HOT;
        } else if (celsiusValue <= 28) {
            prefix = "&e" + HOT;
        } else if (celsiusValue <= 36) {
            prefix = "&6" + HOT;
        } else if (celsiusValue <= 45) {
            prefix = "&c" + HOT;
        } else {
            prefix = "&4" + HOT;
        }
        temp.setTemperatureType(tempType);

        return prefix + " " + fixDouble(temp.getConvertedValue()) + " &7" + tempType.getSuffix();
    }

    public String getAirQualityString(@Nonnull World world, @Nonnull TemperatureType tempType) {
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName()) || world.getEnvironment() != World.Environment.NORMAL) {
            return "&cNon-functional in this world.";
        }

        Temperature temp = new Temperature(15.0);

        double celsiusDifference = (PollutionManager.getPollutionInWorld(world) * GlobalWarmingPlugin.getRegistry().getPollutionMultiply());
        double currentValue = temp.getCelsiusValue() + celsiusDifference;
        double defaultValue = temp.getCelsiusValue();
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

        double difference = celsiusDifference;

        if (tempType != TemperatureType.CELSIUS) {
            difference = getDifference(currentValue, defaultValue, tempType);
        }

        prefix = prefix + (difference > 0 ? "+" : "");

        return prefix + fixDouble(difference) + " &7" + tempType.getSuffix();
    }

    public Temperature addTemperatureChangeFactors(@Nonnull World world, @Nonnull Biome biome, @Nonnull Temperature temperature) {
        double celsiusValue = temperature.getCelsiusValue();
        double nightDrop = 10;

        if (nightDropMap.containsKey(biome)) {
            nightDrop = nightDropMap.get(biome);
        }

        if (world.getEnvironment() == World.Environment.NORMAL) {
            if (!isDaytime(world)) {
                double nightTime = world.getTime() - 12300F;

                if (nightTime > 5775) {
                    nightTime = 5775 - (nightTime - 5775);
                }

                double dropPercent = nightTime / 5775;

                celsiusValue = celsiusValue - (nightDrop * dropPercent);
            } else if (world.hasStorm()) {
                celsiusValue = celsiusValue - GlobalWarmingPlugin.getRegistry().getStormTemperatureDrop();
            }
        }

        celsiusValue = celsiusValue + (PollutionManager.getPollutionInWorld(world) * GlobalWarmingPlugin.getRegistry().getPollutionMultiply());

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

    public static double fixDouble(double amount, int digits) {
        if (digits == 0)
            return (int) amount;
        StringBuilder format = new StringBuilder("##");
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                format.append(".");
            format.append("#");
        }
        return Double.valueOf(new DecimalFormat(format.toString()).format(amount).replace(",", "."));
    }

    public static double fixDouble(double amount) {
        return fixDouble(amount, 2);
    }
}
