package me.poma123.globalwarming;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.poma123.globalwarming.items.Thermometer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    public static final double DEBUG_TEMPERATURE = 24.0;
    private Category category;

    @Override
    public void onEnable() {
        // Read something from your config.yml
        //Config cfg = new Config(this);

        //if (cfg.getBoolean("options.auto-update")) {
            // You could start an Auto-Updater for example
        //}

        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Material.GLASS, "&2Global Warming"));

        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);
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
