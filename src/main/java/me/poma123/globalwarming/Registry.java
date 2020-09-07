package me.poma123.globalwarming;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import org.bukkit.block.Biome;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

public class Registry {
    private final Map<Biome, Double> defaultBiomeTemperatures = new EnumMap<>(Biome.class);

    public void load(Config cfg) {
        for (String biome : cfg.getKeys("default-biome-temperatures")) {
            double celsiusValue = cfg.getDouble("default-biome-temperatures." + biome);

            try {
                defaultBiomeTemperatures.put(Biome.valueOf(biome), celsiusValue);
            } catch (IllegalArgumentException ex) {
                GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load temperature of the invalid Biome \"{0}\"", new Object[] { biome });
            }
        }
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return defaultBiomeTemperatures;
    }
}
