package me.poma123.globalwarming;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.block.Biome;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class Registry {
    private final Map<Biome, Double> defaultBiomeTemperatures = new EnumMap<>(Biome.class);
    private final Set<String> disabledWorlds = new HashSet<>();

    public void load(Config cfg, Config biomes) {
        // Add missing biomes to the config
        for (Biome biome : Biome.values()) {
            if (biomes.getValue("default-biome-temperatures." + biome.name()) == null) {
                biomes.setValue("default-biome-temperatures." + biome.name(), 15);

                GlobalWarming.getInstance().getLogger().log(Level.INFO, "Added missing biome \"{0}\" to biomes.yml with the temperature value of 15", biome);
            }
        }
        biomes.save();

        // Loading default biome temperatures
        for (String biome : biomes.getKeys("default-biome-temperatures")) {
            double celsiusValue = biomes.getDouble("default-biome-temperatures." + biome);

            try {
                defaultBiomeTemperatures.put(Biome.valueOf(biome), celsiusValue);
            } catch (IllegalArgumentException ex) {
                GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load temperature \"{0}\" of the invalid biome \"{1}\"", new Object[] { celsiusValue, biome });
            }
        }

        disabledWorlds.addAll(cfg.getStringList("disabled-worlds"));
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return defaultBiomeTemperatures;
    }

    public boolean isEnabledInWorld(String worldName) {
        return !disabledWorlds.contains(worldName);
    }
}
