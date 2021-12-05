package me.poma123.globalwarming.items;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.Items;

public class CinnabariteResource implements GEOResource {
    private final NamespacedKey key = new NamespacedKey(GlobalWarmingPlugin.getInstance(), "cinnabarite");

    @Override
    public int getDefaultSupply(World.Environment environment, Biome biome) {
        return ThreadLocalRandom.current().nextInt(2) + 2;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxDeviation() {
        return 1;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Cinnabarite";
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return Items.CINNABARITE.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }
}
