package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.TemperatureUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.concurrent.ThreadLocalRandom;

public class FireTask extends MechanicTask {

    private static final int MAX_BLOCKS_PER_CHUNK = 16;
    private static ThreadLocalRandom rnd;

    public FireTask() {
        rnd = ThreadLocalRandom.current();
    }

    private void fire(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;

            for (int i = 0; i < 10; i++) {
                int index = rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() * MAX_BLOCKS_PER_CHUNK) + rnd.nextInt(MAX_BLOCKS_PER_CHUNK);
                int z = (chunk.getZ() * MAX_BLOCKS_PER_CHUNK) + rnd.nextInt(MAX_BLOCKS_PER_CHUNK);

                Block block = world.getHighestBlockAt(x, z).getRelative(BlockFace.UP);

                if (TemperatureUtils.getTemperatureAtLocation(block.getLocation()).getCelsiusValue() >= 40) {
                    block.setType(Material.FIRE);
                }
            }
        }
    }

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            if (GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && !(w.hasStorm() || w.isThundering()) && w.getLoadedChunks().length > 0) {
                int rndInt = rnd.nextInt(10);

                if (rndInt < 3) {
                    fire(w);
                }
            }
        }
    }
}