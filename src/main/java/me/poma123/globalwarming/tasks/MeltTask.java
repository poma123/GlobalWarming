package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.TemperatureUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFadeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class MeltTask extends MechanicTask {

    private static final int MAX_BLOCKS_PER_CHUNK = 16;
    private static ThreadLocalRandom rnd;

    public MeltTask() {
        rnd = ThreadLocalRandom.current();
    }

    private void melt(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;

            for (int i = 0; i < 10; i++) {
                int index = rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() * MAX_BLOCKS_PER_CHUNK) + rnd.nextInt(MAX_BLOCKS_PER_CHUNK);
                int z = (chunk.getZ() * MAX_BLOCKS_PER_CHUNK) + rnd.nextInt(MAX_BLOCKS_PER_CHUNK);

                Block iceBlock = world.getHighestBlockAt(x, z);

                if (Tag.ICE.isTagged(iceBlock.getType()) && TemperatureUtils.getTemperatureAtLocation(iceBlock.getLocation()).getCelsiusValue() >= 2) {
                    BlockState state = iceBlock.getState();
                    state.setType(Material.WATER);

                    GlobalWarming.getInstance().getServer().getPluginManager().callEvent(new BlockFadeEvent(iceBlock, state));
                }
            }
        }
    }

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            if (GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && w.getLoadedChunks().length > 0) {
                int rndInt = rnd.nextInt(10);

                if (rndInt < 3) {
                    melt(w);
                }
            }
        }
    }
}
