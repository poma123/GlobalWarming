package me.poma123.globalwarming;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.block.Biome;

import java.util.EnumMap;
import java.util.Map;

public class Registry {

    private final Map<Biome, Double> biomeTemperatures = new EnumMap<>(Biome.class);

    public void load(Config cfg) {

        for (String biome : cfg.getKeys("default-biome-temperatures")) {
            biomeTemperatures.put(Biome.valueOf(biome), 2.0);
        }
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return biomeTemperatures;
    }
}
