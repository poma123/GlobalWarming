package me.poma123.globalwarming.items;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.Items;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

public class CinnabariteResource implements GEOResource {
    private final NamespacedKey key = new NamespacedKey(GlobalWarming.getInstance(), "cinnabarite");

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
