package me.poma123.globalwarming.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.poma123.globalwarming.utils.PollutionUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PollutionListener implements Listener {

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        Block b = e.getMachine();
        MachineRecipe machineRecipe = e.getMachineRecipe();


        for (ItemStack item : machineRecipe.getInput()) {
            double value = PollutionUtils.isPolluted(item);

            if (value > 0.0) {
                PollutionUtils.risePollutionInWorld(b.getLocation().getWorld(), value);
            }
        }
    }
}
