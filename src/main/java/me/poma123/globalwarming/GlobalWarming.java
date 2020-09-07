package me.poma123.globalwarming;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.CoalGenerator;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.poma123.globalwarming.items.Thermometer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    private static final Registry registry = new Registry();
    private Category category;

    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);

        // Add missing biomes to the config
        for (Biome biome : Biome.values()) {
            if (cfg.getValue("default-biome-temperatures." + biome.name()) == null) {
                cfg.setValue("default-biome-temperatures." + biome.name(), 15);
            }
        }

        cfg.save();

        registry.load(cfg);
        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Material.GLASS, "&2Global Warming"));

        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);
    }

    public static Registry getRegistry() {
        return registry;
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
