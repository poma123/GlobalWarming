package me.poma123.globalwarming.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.PollutionUtils;

public class PollutionListener implements Listener {

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        World world = e.getMachine().getWorld();
        MachineRecipe machineRecipe = e.getMachineRecipe();
        double pollutionValue = 0.0;

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        for (ItemStack item : machineRecipe.getInput()) {
            double value = PollutionUtils.isPollutedItem(item);

            if (value > 0.0) {
                pollutionValue += value;
            }
        }

        if (pollutionValue > 0.0) {
            PollutionUtils.risePollutionInWorld(world, pollutionValue);
        }
    }
}
