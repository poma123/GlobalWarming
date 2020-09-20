package me.poma123.globalwarming.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class AirQualityMeter extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public AirQualityMeter(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        SlimefunItem.registerBlockHandler(getID(), (p, b, stack, reason) -> {
            SimpleHologram.remove(b);
            return true;
        });
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                SimpleHologram.update(e.getBlock(), "&7Measuring...");
            }
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace());
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                AirQualityMeter.this.tick(b);
            }
        });
    }

    private void tick(@Nonnull Block b) {
        //SimpleHologram.update(b, TemperatureUtils.getAirQualityString(TemperatureType.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "type"))));
        SimpleHologram.update(b, "&cNot finished yet.");
    }
}
