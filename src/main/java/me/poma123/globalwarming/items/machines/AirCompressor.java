package me.poma123.globalwarming.items.machines;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.Items;

public abstract class AirCompressor extends AContainer implements RecipeDisplayItem {

    public static final RecipeType RECIPE_TYPE = new RecipeType(
            new NamespacedKey(GlobalWarmingPlugin.getInstance(), "air_compressor"), Items.AIR_COMPRESSOR
    );

    protected AirCompressor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
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
