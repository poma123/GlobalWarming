package me.poma123.globalwarming;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;

public class Registry {
    private double pollutionMultiply;
    private double treeGrowthAbsorbtion;
    private double animalBreedPollution;

    private final Map<Biome, Double> defaultBiomeTemperatures = new EnumMap<>(Biome.class);
    private final Set<String> enabledWorlds = new HashSet<>();
    private final Map<String, Config> worldConfigs = new HashMap<>();
    private final Map<Material, Double> pollutedVanillaItems = new EnumMap<>(Material.class);
    private final Map<String, Double> pollutedSlimefunItems = new HashMap<>();
    private final Map<String, Double> pollutedSlimefunMachines = new HashMap<>();
    private final Map<String, Double> absorbentSlimefunMachines = new HashMap<>();

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
            }
            catch (IllegalArgumentException ex) {
                GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load temperature \"{0}\" of the invalid biome \"{1}\"", new Object[] { celsiusValue, biome });
            }
        }

        List<String> disabledWorlds = cfg.getStringList("disabled-worlds");

        // Creating world configs
        for (World w : Bukkit.getWorlds()) {
            if (!disabledWorlds.contains(w.getName())) {
                enabledWorlds.add(w.getName());

                getWorldConfig(w);
            }
        }

        // Registering pollution productuon

        Bukkit.getScheduler().runTaskLater(GlobalWarming.getInstance(), () -> {
            // Registering polluting items
            for (String id : cfg.getKeys("pollution.production.machine-recipe-input-items")) {
                double value = cfg.getDouble("pollution.production.machine-recipe-input-items." + id);

                if (value <= 0.0) {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load polluted item \"{0}\" with an invalid pollution value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (Material.getMaterial(id) != null) {
                    pollutedVanillaItems.put(Material.getMaterial(id), value);
                } else if (SlimefunItem.getByID(id) != null) {
                    pollutedSlimefunItems.put(id, value);
                } else {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load polluted item \"{0}\" with a pollution value of \"{1}\"", new Object[] { id, value });
                }
            }

            // Registering polluting machines
            for (String id : cfg.getKeys("pollution.production.machines")) {
                double value = cfg.getDouble("pollution.production.machines." + id);

                if (value <= 0.0) {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load polluted machine \"{0}\" with an invalid pollution value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (SlimefunItem.getByID(id) != null) {
                    pollutedSlimefunMachines.put(id, value);
                } else {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load polluted machine \"{0}\" with a pollution value of \"{1}\"", new Object[] { id, value });
                }
            }

            // Registering absorbent machines
            for (String id : cfg.getKeys("pollution.absorbtion.machines")) {
                double value = cfg.getDouble("pollution.absorbtion.machines." + id);

                if (value <= 0.0) {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load absorbent machine \"{0}\" with an invalid pollution value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (SlimefunItem.getByID(id) != null) {
                    absorbentSlimefunMachines.put(id, value);
                } else {
                    GlobalWarming.getInstance().getLogger().log(Level.WARNING, "Could not load absorbent machine \"{0}\" with a pollution value of \"{1}\"", new Object[] { id, value });
                }
            }
        }, 100);

        pollutionMultiply = cfg.getOrSetDefault("pollution.options.pollution-multiply", 0.002);
        treeGrowthAbsorbtion = cfg.getOrSetDefault("pollution.absorbtion.tree-growth", 0.01);
        animalBreedPollution = cfg.getOrSetDefault("pollution.production.animal-breed", 0.007);
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return defaultBiomeTemperatures;
    }

    public boolean isWorldEnabled(@Nonnull String worldName) {
        return enabledWorlds.contains(worldName);
    }

    public Set<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    @Nullable
    public Config getWorldConfig(@Nonnull World world) {
        if (isWorldEnabled(world.getName())) {
            if (!worldConfigs.containsKey(world.getName())) {
                worldConfigs.put(world.getName(), getNewWorldConfig(world));
            }
            return worldConfigs.get(world.getName());
        }
        return null;
    }

    public Config getNewWorldConfig(@Nonnull World world) {
        Config config = new Config(GlobalWarming.getInstance(), "worlds/" + world.getName() + ".yml");
        if (config.getValue("data.pollution") == null) {
            config.setValue("data.pollution", 0.0);
            config.save();
        }

        return config;
    }

    public Map<Material, Double> getPollutedVanillaItems() {
        return pollutedVanillaItems;
    }

    public Map<String, Double> getPollutedSlimefunItems() {
        return pollutedSlimefunItems;
    }

    public Map<String, Double> getPollutedSlimefunMachines() {
        return pollutedSlimefunMachines;
    }

    public Map<String, Double> getAbsorbentSlimefunMachines() {
        return absorbentSlimefunMachines;
    }

    public double getPollutionMultiply() {
        return pollutionMultiply;
    }

    public double getTreeGrowthAbsorbtion() {
        return treeGrowthAbsorbtion;
    }

    public double getAnimalBreedPollution() {
        return animalBreedPollution;
    }
}
