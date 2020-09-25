package me.poma123.globalwarming.items;

import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.poma123.globalwarming.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AirCompressor extends AContainer implements RecipeDisplayItem {

    public AirCompressor(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(10, Items.EMPTY_CANISTER, Items.CO2_CANISTER);
    }

    private void addRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output });
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.HOPPER);
    }

    @Override
    public String getMachineIdentifier() {
        return "AIR_COMPRESSOR";
    }
}
