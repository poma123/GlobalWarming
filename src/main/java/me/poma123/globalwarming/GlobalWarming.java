package me.poma123.globalwarming;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.poma123.globalwarming.items.AirQualityMeter;
import me.poma123.globalwarming.items.Thermometer;
import me.poma123.globalwarming.listeners.PollutionListener;
import me.poma123.globalwarming.tasks.FireTask;
import me.poma123.globalwarming.tasks.MeltTask;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    private Config cfg;
    private Config biomes;
    private static GlobalWarming instance;
    private static final Registry registry = new Registry();
    private Category category;

    @Override
    public void onEnable() {
        instance = this;

        // Create configuration files
        cfg = new Config(this);

        final File biomesFile = new File(getDataFolder(), "biomes.yml");
        if (!biomesFile.exists()) {
            try {
                Files.copy(this.getClass().getResourceAsStream("/biomes.yml"), biomesFile.toPath());
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to create default biomes.yml file", e);
            }
        }
        biomes = new Config(this, "biomes.yml");

        registry.load(cfg, biomes);

        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Items.THERMOMETER, "&2Global Warming"));

        // Empty craft for now...
        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);

        new AirQualityMeter(category, Items.AIR_QUALITY_METER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);

        new FireTask().scheduleRepeating(0, 20);
        new MeltTask().scheduleRepeating(0, 20);

        Bukkit.getPluginManager().registerEvents(new PollutionListener(), this);
    }

    public static Registry getRegistry() {
        return registry;
    }

    public static GlobalWarming getInstance() {
        return instance;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/poma123/GlobalWarming/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
