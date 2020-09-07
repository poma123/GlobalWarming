package me.poma123.globalwarming;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.block.Biome;

import java.util.EnumMap;
import java.util.Map;

public class Registry {
    private final Map<Biome, Double> defaultBiomeTemperatures = new EnumMap<>(Biome.class);

    public void load(Config cfg) {
        for (String biome : cfg.getKeys("default-biome-temperatures")) {
            defaultBiomeTemperatures.put(Biome.valueOf(biome), cfg.getDouble("default-biome-temperatures." + biome));
        }
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return defaultBiomeTemperatures;
    }
}
