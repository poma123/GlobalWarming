package me.poma123.globalwarming.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import me.poma123.globalwarming.GlobalWarmingPlugin;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();

        GlobalWarmingPlugin.getRegistry().registerWorld(world, world.getName());
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent e) {
        World world = e.getWorld();

        GlobalWarmingPlugin.getRegistry().unregisterWorld(world.getName());
    }
}
