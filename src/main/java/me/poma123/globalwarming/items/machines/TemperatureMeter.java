package me.poma123.globalwarming.items.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.poma123.globalwarming.api.TemperatureType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class TemperatureMeter extends SlimefunItem implements HologramOwner {

    @ParametersAreNonnullByDefault
    protected TemperatureMeter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                removeHologram(b);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b,"type", TemperatureType.CELSIUS.name());
                updateHologram(b, "&7Measuring...");
            }
        };
    }

    @Nonnull
    private BlockUseHandler onRightClick() {
        return e -> {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            TemperatureType saved = TemperatureType.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "type"));

            if (saved == TemperatureType.CELSIUS) {
                saved = TemperatureType.FAHRENHEIT;
            } else if (saved == TemperatureType.FAHRENHEIT) {
                saved = TemperatureType.KELVIN;
            } else {
                saved = TemperatureType.CELSIUS;
            }

            BlockStorage.addBlockInfo(b, "type", saved.name());
            p.sendMessage("§7Temperature type: §e" + saved.getName());

            e.cancel();
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onBreak());
        addItemHandler(onPlace());
        addItemHandler(onRightClick());
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                TemperatureMeter.this.tick(b);
            }
        });
    }

    public void tick(@Nonnull Block b) {
    }
}
