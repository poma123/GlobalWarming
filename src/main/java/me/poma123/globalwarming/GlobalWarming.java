package me.poma123.globalwarming;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.poma123.globalwarming.items.Thermometer;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    private Config cfg;
    private static GlobalWarming instance;
    private static final Registry registry = new Registry();
    private Category category;

    @Override
    public void onEnable() {
        instance = this;

        // Read something from your config.yml
        cfg = new Config(this);

        // Add missing biomes to the config
        for (Biome biome : Biome.values()) {
            if (cfg.getValue("options.default-biome-temperatures." + biome.name()) == null) {
                cfg.setValue("options.default-biome-temperatures." + biome.name(), 15);
            }
        }

        cfg.save();
        registry.load(cfg);

        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Items.THERMOMETER, "&2Global Warming"));

        // Empty craft for now...
        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);
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
