package me.poma123.globalwarming.items.machines;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.poma123.globalwarming.Items;

public abstract class AirCompressor extends AContainer implements RecipeDisplayItem {

    public static final RecipeType RECIPE_TYPE = new RecipeType(
            new NamespacedKey(GlobalWarmingPlugin.getInstance(), "air_compressor"), Items.AIR_COMPRESSOR
    );

    protected AirCompressor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(5, Items.EMPTY_CANISTER, new ItemStack[] { Items.CO2_CANISTER });
        addRecipe(5, Items.CO2_CANISTER, new ItemStack[] { SlimefunItems.CARBON, Items.EMPTY_CANISTER });
    }

    private void addRecipe(int seconds, ItemStack input, ItemStack[] output) {
        registerRecipe(seconds, new ItemStack[] { input }, output);
    }

    @Override
    public String getMachineIdentifier() {
        return "AIR_COMPRESSOR";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.HOPPER);
    }
}
