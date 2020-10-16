package me.poma123.globalwarming.tasks;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFadeEvent;

import me.poma123.globalwarming.GlobalWarmingPlugin;

public class MeltTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double minimumTemperature;
    private final double chance;
    private final int meltAmount;

    @ParametersAreNonnullByDefault
    public MeltTask(double minimumTemperature, double chance, int meltAmount) {
        rnd = ThreadLocalRandom.current();
        this.minimumTemperature = minimumTemperature;
        this.chance = chance;
        this.meltAmount = meltAmount;
    }

    private void melt(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;

            for (int i = 0; i < meltAmount; i++) {
                int index = rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() << 4) + rnd.nextInt(16);
                int z = (chunk.getZ() << 4) + rnd.nextInt(16);

                Block current = world.getHighestBlockAt(x, z);
                if (Tag.ICE.isTagged(current.getType()) && GlobalWarmingPlugin.getTemperatureManager().getTemperatureAtLocation(current.getLocation()).getCelsiusValue() >= minimumTemperature) {
                    BlockState state = current.getState();

                    if (current.getType() == Material.ICE) {
                        state.setType(Material.WATER);
                    } else {
                        state.setType(Material.AIR);
                    }

                    GlobalWarmingPlugin.getInstance().getServer().getPluginManager().callEvent(new BlockFadeEvent(current, state));
                }
            }
        }
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) && w.getEnvironment() == World.Environment.NORMAL && !w.getPlayers().isEmpty() && w.getLoadedChunks().length > 0) {
                double random = rnd.nextDouble();

                if (random < chance) {
                    melt(w);
                }
            }
        }
    }
}