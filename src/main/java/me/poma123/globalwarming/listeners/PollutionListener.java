package me.poma123.globalwarming.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.poma123.globalwarming.utils.PollutionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class PollutionListener implements Listener {

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        Block b = e.getMachine();
        MachineRecipe machineRecipe = e.getMachineRecipe();

        PollutionUtils.risePollutionInWorld(b.getLocation().getWorld(), 0.1);
        /*if (Arrays.stream(machineRecipe.getInput()).anyMatch(i -> (i.getType().equals(Material.COAL)))) {

        }*/
    }
}
