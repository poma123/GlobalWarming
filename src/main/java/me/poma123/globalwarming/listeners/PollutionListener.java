package me.poma123.globalwarming.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncGeneratorProcessCompleteEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncReactorProcessCompleteEvent;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.PollutionUtils;

public class PollutionListener implements Listener {

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        double pollutionValue = calculatePollutionValue(e.getMachine().getID(), e.getMachineRecipe().getInput());

        if (pollutionValue > 0.0) {
            PollutionUtils.risePollutionInWorld(world, pollutionValue);
        }
    }

    @EventHandler
    public void onGeneratorProcessComplete(AsyncGeneratorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        double pollutionValue = calculatePollutionValue(e.getGenerator().getID(), new ItemStack[]{ e.getMachineFuel().getInput() });

        if (pollutionValue > 0.0) {
            PollutionUtils.risePollutionInWorld(world, pollutionValue);
        }
    }

    @EventHandler
    public void onReactorProcessComplete(AsyncReactorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        double pollutionValue = calculatePollutionValue(e.getReactor().getID(), new ItemStack[]{ e.getMachineFuel().getInput() });

        if (pollutionValue > 0.0) {
            PollutionUtils.risePollutionInWorld(world, pollutionValue);
        }
    }

    private double calculatePollutionValue(String ID, ItemStack[] recipeInput) {
        double pollutionValue = 0.0;
        
        pollutionValue += PollutionUtils.isPollutedMachine(ID);

        for (ItemStack item : recipeInput) {
            pollutionValue += PollutionUtils.isPollutedItem(item);
        }
        
        return pollutionValue;
    }
}
