package me.poma123.globalwarming.tasks;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.TemperatureUtils;

public class FireTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double minimumTemperature;
    private final double chance;
    private final int fireAmount;

    @ParametersAreNonnullByDefault
    public FireTask(double minimumTemperature, double chance, int fireAmount) {
        rnd = ThreadLocalRandom.current();
        this.minimumTemperature = minimumTemperature;
        this.chance = chance;
        this.fireAmount = fireAmount;
    }

    private void fire(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;

            for (int i = 0; i < fireAmount; i++) {
                int index = rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() << 4) + rnd.nextInt(16);
                int z = (chunk.getZ() << 4) + rnd.nextInt(16);

                Block current = world.getHighestBlockAt(x, z).getRelative(BlockFace.UP);
                if (TemperatureUtils.getTemperatureAtLocation(current.getLocation()).getCelsiusValue() >= minimumTemperature) {
                    current.setType(Material.FIRE);
                }
            }
        }
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarming.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && !(w.hasStorm() || w.isThundering()) && w.getLoadedChunks().length > 0) {
                double random = rnd.nextDouble();

                if (random < chance) {
                    fire(w);
                }
            }
        }
    }
}