package me.poma123.globalwarming;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;

import me.poma123.globalwarming.api.WorldFilterType;

public class Registry {

    private final List<String> news = new ArrayList<>();
    private final Map<Biome, Double> defaultBiomeTemperatures = new EnumMap<>(Biome.class);
    private final Map<Biome, Double> maxTemperatureDropsAtNight = new EnumMap<>(Biome.class);
    private final Set<String> enabledWorlds = new HashSet<>();
    private final Map<String, Config> worldConfigs = new HashMap<>();
    private final Map<Material, Double> pollutedVanillaItems = new EnumMap<>(Material.class);
    private final Map<String, Double> pollutedSlimefunItems = new HashMap<>();
    private final Map<String, Double> pollutedSlimefunMachines = new HashMap<>();
    private final Map<String, Double> absorbentSlimefunMachines = new HashMap<>();
    private final Set<String> worlds = new HashSet<>();
    private WorldFilterType worldFilterType;
    private double pollutionMultiply;
    private double stormTemperatureDrop;
    private double treeGrowthAbsorption;
    private double animalBreedPollution;
    private Research researchNeededForPlayerMechanics = null;

    public void load(Config cfg, Config biomes, Config messages) {
        // Add missing biomes to the config
        for (Biome biome : Biome.values()) {
            if (biomes.getValue("default-biome-temperatures." + biome.name()) == null) {
                biomes.setValue("default-biome-temperatures." + biome.name(), 15);

                GlobalWarmingPlugin.getInstance().getLogger().log(Level.INFO, "Added missing biome \"{0}\" to biomes.yml with the temperature value of 15", biome);
            }
        }
        biomes.save();

        // Loading default biome temperatures
        for (String biome : biomes.getKeys("default-biome-temperatures")) {
            double celsiusValue = biomes.getDouble("default-biome-temperatures." + biome);

            try {
                defaultBiomeTemperatures.put(Biome.valueOf(biome), celsiusValue);
            } catch (IllegalArgumentException ex) {
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load temperature \"{0}\" of the invalid biome \"{1}\"", new Object[] { celsiusValue, biome });
            }
        }

        // Loading night temperature drops
        for (String biome : biomes.getKeys("max-temperature-drop-at-night")) {
            double celsiusValue = biomes.getDouble("max-temperature-drop-at-night." + biome);

            try {
                maxTemperatureDropsAtNight.put(Biome.valueOf(biome), celsiusValue);
            } catch (IllegalArgumentException ex) {
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load temperature drop \"{0}\" of the invalid biome \"{1}\"", new Object[] { celsiusValue, biome });
            }
        }

        // Whitelisting or blacklisting worlds
        List<String> oldDisabledWorlds = cfg.getStringList("disabled-worlds");
        if (!oldDisabledWorlds.isEmpty()) {
            cfg.setValue("worlds", oldDisabledWorlds);
            cfg.setValue("disabled-worlds", null);
            cfg.setValue("world-filter-type", "blacklist");
            cfg.save();
        }

        try {
            worldFilterType = WorldFilterType.valueOf(((String) cfg.getOrSetDefault("world-filter-type", "blacklist")).toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            worldFilterType = WorldFilterType.BLACKLIST;
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "\"{0}\" is not a valid world filter type. Now using default value (blacklist)", new Object[] { cfg.getString("world-filter-type") });
        }
        
        worlds.addAll(cfg.getStringList("worlds"));

        for (World w : Bukkit.getWorlds()) {
            registerWorld(w, w.getName());
        }

        // Registering pollution production
        // We are delaying this so that we can register items from other addons
        Bukkit.getScheduler().runTaskLater(GlobalWarmingPlugin.getInstance(), () -> {
            // Registering polluting items
            for (String id : cfg.getKeys("pollution.production.machine-recipe-input-items")) {
                double value = cfg.getDouble("pollution.production.machine-recipe-input-items." + id);

                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load polluted item \"{0}\" with an invalid pollution value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (Material.getMaterial(id) != null) {
                    pollutedVanillaItems.put(Material.getMaterial(id), value);
                } else if (SlimefunItem.getById(id) != null) {
                    pollutedSlimefunItems.put(id, value);
                } else {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load non-existent polluted item \"{0}\" with a pollution value of \"{1}\"", new Object[] { id, value });
                }
            }

            // Registering polluting machines
            for (String id : cfg.getKeys("pollution.production.machines")) {
                double value = cfg.getDouble("pollution.production.machines." + id);

                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load polluted machine \"{0}\" with an invalid pollution value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (SlimefunItem.getById(id) != null) {
                    pollutedSlimefunMachines.put(id, value);
                } else {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load non-existent polluted machine \"{0}\" with a pollution value of \"{1}\"", new Object[] { id, value });
                }
            }

            // Registering absorbent machines
            for (String id : cfg.getKeys("pollution.absorption.machines")) {
                double value = cfg.getDouble("pollution.absorption.machines." + id);

                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load absorbent machine \"{0}\" with an invalid absorption value of \"{1}\"", new Object[] { id, value });
                    continue;
                }

                if (SlimefunItem.getById(id) != null) {
                    absorbentSlimefunMachines.put(id, value);
                } else {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load non-existent absorbent machine \"{0}\" with an absorption value of \"{1}\"", new Object[] { id, value });
                }
            }
        }, 100);

        news.addAll(messages.getStringList("messages.news"));

        pollutionMultiply = cfg.getOrSetDefault("temperature-options.pollution-multiply", 0.002);
        stormTemperatureDrop = cfg.getOrSetDefault("temperature-options.temperature-drop-during-storms", 8);
        treeGrowthAbsorption = cfg.getOrSetDefault("pollution.absorption.tree-growth", 0.01);
        animalBreedPollution = cfg.getOrSetDefault("pollution.production.animal-breed", 0.007);

        String researchKey = cfg.getString("needed-research-for-player-mechanics");
        Optional<Research> tempResearch = Research.getResearch(new NamespacedKey(Slimefun.instance(), researchKey));

        if (tempResearch.isPresent() && tempResearch.get().isEnabled()) {
            researchNeededForPlayerMechanics = tempResearch.get();
        } else {
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load research \"{0}\"", new Object[] { researchKey });
        }
    }

    public Map<Biome, Double> getDefaultBiomeTemperatures() {
        return defaultBiomeTemperatures;
    }

    public Map<Biome, Double> getMaxTemperatureDropsAtNight() {
        return maxTemperatureDropsAtNight;
    }

    public boolean isWorldEnabled(@Nonnull String worldName) {
        if (Bukkit.getWorld(worldName) == null) {
            enabledWorlds.remove(worldName);
            return false;
        }
        return enabledWorlds.contains(worldName);
    }

    public void registerWorld(World w, String worldName) {
        if (worldFilterType == WorldFilterType.BLACKLIST) {
            if (!worlds.contains(worldName)) {
                enabledWorlds.add(worldName);
                getWorldConfig(w);
            }
        } else {
            if (worlds.contains(worldName)) {
                enabledWorlds.add(worldName);
                getWorldConfig(w);
            }
        }
    }

    public void unregisterWorld(String worldName) {
        enabledWorlds.remove(worldName);
    }

    public Set<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    @Nullable
    public Config getWorldConfig(@Nullable World world) {
        if (world != null && isWorldEnabled(world.getName())) {
            if (!worldConfigs.containsKey(world.getName())) {
                worldConfigs.put(world.getName(), getNewWorldConfig(world));
            }
            return worldConfigs.get(world.getName());
        }
        return null;
    }

    public Config getNewWorldConfig(@Nonnull World world) {
        Config config = new Config(GlobalWarmingPlugin.getInstance(), "worlds/" + world.getName() + ".yml");
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

    public List<String> getNews() {
        return news;
    }

    public double getPollutionMultiply() {
        return pollutionMultiply;
    }

    public double getStormTemperatureDrop() {
        return stormTemperatureDrop;
    }

    public double getTreeGrowthAbsorption() {
        return treeGrowthAbsorption;
    }

    public double getAnimalBreedPollution() {
        return animalBreedPollution;
    }

    public Research getResearchNeededForPlayerMechanics() {
        return researchNeededForPlayerMechanics;
    }
}
