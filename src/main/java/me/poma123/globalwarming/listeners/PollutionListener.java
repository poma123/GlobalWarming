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
        String ID;
        ItemStack[] recipeInput;
        double pollutionValue = 0.0;

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        if (e instanceof AsyncGeneratorProcessCompleteEvent) {
            AsyncGeneratorProcessCompleteEvent event =  (AsyncGeneratorProcessCompleteEvent) e;

            ID = event.getGenerator().getID();
            recipeInput = new ItemStack[]{ event.getMachineFuel().getInput() };
        } else if (e instanceof AsyncReactorProcessCompleteEvent) {
            AsyncReactorProcessCompleteEvent event =  (AsyncReactorProcessCompleteEvent) e;

            ID = event.getReactor().getID();
            recipeInput = new ItemStack[]{ event.getMachineFuel().getInput() };
        } else {

            ID = e.getMachine().getID();
            recipeInput = e.getMachineRecipe().getInput();
        }

        pollutionValue += PollutionUtils.isPollutedMachine(ID);

        for (ItemStack item : recipeInput) {
            pollutionValue += PollutionUtils.isPollutedItem(item);
        }

        if (pollutionValue > 0.0) {
            PollutionUtils.risePollutionInWorld(world, pollutionValue);
        }
    }
}
