package me.poma123.globalwarming;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.poma123.globalwarming.items.Thermometer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalWarming extends JavaPlugin implements SlimefunAddon {

    private final Registry registry = new Registry();
    private Category category;

    @Override
    public void onEnable() {
        // Read something from your config.yml
        Config cfg = new Config(this);

        //if (cfg.getBoolean("options.auto-update")) {
            // You could start an Auto-Updater for example
        //}

        registry.load(cfg);

        GitHubBuildsUpdater updater = new GitHubBuildsUpdater(this, getFile(), "");

        category = new Category(new NamespacedKey(this, "global_warming"), new CustomItem(Material.GLASS, "&2Global Warming"));

        new Thermometer(category, Items.THERMOMETER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null
        }).register(this);
    }

    public Registry getRegistry() {
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
