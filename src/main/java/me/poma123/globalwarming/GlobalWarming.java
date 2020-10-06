package me.poma123.globalwarming;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.poma123.globalwarming.items.machines.AirQualityMeter;
import me.poma123.globalwarming.items.machines.Thermometer;
import me.poma123.globalwarming.items.CinnabariteResource;
import me.poma123.globalwarming.listeners.PollutionListener;
import me.poma123.globalwarming.tasks.FireTask;
import me.poma123.globalwarming.tasks.MeltTask;
import me.poma123.globalwarming.tasks.SlownessTask;
import me.poma123.globalwarming.items.machines.AirCompressor;
import me.poma123.globalwarming.tasks.BurnTask;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    private static Config cfg;
    private static Config messages;
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
            }
            catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to create default biomes.yml file", e);
            }
        }
        biomes = new Config(this, "biomes.yml");

        final File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            try {
                Files.copy(this.getClass().getResourceAsStream("/messages.yml"), messagesFile.toPath());
            }
            catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to create default messages.yml file", e);
            }
        }
        messages = new Config(this, "messages.yml");

        registerItems();
        registry.load(cfg, biomes, messages);
        scheduleTasks();

        Bukkit.getPluginManager().registerEvents(new PollutionListener(), this);
    }

    private void registerItems() {
        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Items.THERMOMETER, "&2Global Warming"));

        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.NICKEL_INGOT, new ItemStack(Material.GLASS), SlimefunItems.NICKEL_INGOT,
                SlimefunItems.NICKEL_INGOT, Items.MERCURY, SlimefunItems.NICKEL_INGOT,
                SlimefunItems.NICKEL_INGOT, new ItemStack(Material.GLASS), SlimefunItems.NICKEL_INGOT
        }).register(this);

        new AirQualityMeter(category, Items.AIR_QUALITY_METER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT, SlimefunItems.BILLON_INGOT,
                SlimefunItems.SOLDER_INGOT, Items.THERMOMETER, SlimefunItems.SOLDER_INGOT,
                SlimefunItems.SOLDER_INGOT, SlimefunItems.MAGNET, SlimefunItems.SOLDER_INGOT
        }).register(this);

        new AirCompressor(category, Items.AIR_COMPRESSOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.SOLDER_INGOT, Items.FILTER, SlimefunItems.SOLDER_INGOT,
                SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT,
                SlimefunItems.SOLDER_INGOT, SlimefunItems.BATTERY, SlimefunItems.SOLDER_INGOT
        }) {
            @Override
            public int getEnergyConsumption() {
                return 16;
            }

            @Override
            public int getCapacity() {
                return 512;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.register(this);

        new SlimefunItem(category, Items.EMPTY_CANISTER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, SlimefunItems.SOLDER_INGOT, null,
                SlimefunItems.SOLDER_INGOT, new ItemStack(Material.GLASS_BOTTLE), SlimefunItems.SOLDER_INGOT,
                SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT, SlimefunItems.SOLDER_INGOT
        }).register(this);

        new SimpleSlimefunItem<ItemConsumptionHandler>(category, Items.CO2_CANISTER, AirCompressor.RECIPE_TYPE, new ItemStack[] {
                null, null, null,
                null, Items.EMPTY_CANISTER, null,
                null, null, null
        }) {
            @Override
            public ItemConsumptionHandler getItemHandler() {
                return (e, p, item) -> {
                    e.setCancelled(true);
                };
            }
        }.register(this);

        new SlimefunItem(category, Items.CINNABARITE, RecipeType.GEO_MINER, new ItemStack[]{}).register(this);
        new CinnabariteResource().register();

        new SlimefunItem(category, Items.MERCURY, RecipeType.SMELTERY, new ItemStack[]{
                Items.CINNABARITE, null, null,
                null, null, null,
                null, null, null
        }).register(this);

        new SlimefunItem(category, Items.FILTER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.GLASS), null,
                new ItemStack(Material.GLASS), SlimefunItems.GOLD_PAN, new ItemStack(Material.GLASS),
                null, new ItemStack(Material.GLASS), null
        }).register(this);
    }

    private void scheduleTasks() {
        if (cfg.getBoolean("mechanics.FOREST_FIRES.enabled")) {
            new FireTask(cfg.getOrSetDefault("mechanics.FOREST_FIRES.min-temperature-in-celsius", 40.0),
                    cfg.getOrSetDefault("mechanics.FOREST_FIRES.chance", 0.3),
                    cfg.getOrSetDefault("mechanics.FOREST_FIRES.fire-per-second", 10)
            ).scheduleRepeating(0, 20);
        }

        if (cfg.getBoolean("mechanics.ICE_MELTING.enabled")) {
            new MeltTask(cfg.getOrSetDefault("mechanics.ICE_MELTING.min-temperature-in-celsius", 2.0),
                    cfg.getOrSetDefault("mechanics.ICE_MELTING.chance", 0.5),
                    cfg.getOrSetDefault("mechanics.ICE_MELTING.melt-per-second", 10)
            ).scheduleRepeating(0, 20);
        }

        if (cfg.getBoolean("mechanics.SLOWNESS.enabled")) {
            new SlownessTask(cfg.getOrSetDefault("mechanics.SLOWNESS.chance", 0.8)).scheduleRepeating(0, 200);
        }

        if (cfg.getBoolean("mechanics.BURNING.enabled")) {
            new BurnTask(cfg.getOrSetDefault("mechanics.BURNING.chance", 0.8)).scheduleRepeating(0, 200);
        }
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

    public static Config getCfg() {
        return cfg;
    }

    public static Config getMessages() {
        return messages;
    }
}
