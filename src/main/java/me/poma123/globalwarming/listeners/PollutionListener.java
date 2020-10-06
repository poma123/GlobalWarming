package me.poma123.globalwarming.listeners;


import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AsyncGeneratorProcessCompleteEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncReactorProcessCompleteEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncMachineProcessCompleteEvent;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.math.DoubleHandler;
import me.poma123.globalwarming.api.events.AsyncWorldPollutionChangeEvent;
import me.poma123.globalwarming.api.TemperatureType;
import me.poma123.globalwarming.api.PollutionManager;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.utils.TemperatureUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PollutionListener implements Listener {

    private static final int BROADCAST_COOLDOWN = 60000;

    private final Map<String, Long> lastWorldBroadcasts = new HashMap<>();
    private final Map<String, Double> tempPollutionValues = new HashMap<>();

    @EventHandler
    public void onMachineProcessComplete(AsyncMachineProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getMachine().getID(), e.getMachineRecipe().getInput());

        descendPollutionTry(world, e.getMachine().getID());
    }

    @EventHandler
    public void onGeneratorProcessComplete(AsyncGeneratorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getGenerator().getID(), new ItemStack[]{ e.getMachineFuel().getInput() });

        descendPollutionTry(world, e.getGenerator().getID());
    }

    @EventHandler
    public void onReactorProcessComplete(AsyncReactorProcessCompleteEvent e) {
        World world = e.getLocation().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        risePollutionTry(world, e.getReactor().getID(), new ItemStack[]{ e.getMachineFuel().getInput() });

        descendPollutionTry(world, e.getReactor().getID());
    }

    @EventHandler
    public void onAnimalBreed(EntityBreedEvent e) {
        World world = e.getMother().getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        double pollutionValue = GlobalWarming.getRegistry().getAnimalBreedPollution();

        if (pollutionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, pollutionValue);
        }
    }

    @EventHandler
    public void onTreeGrowth(StructureGrowEvent e) {
        World world = e.getWorld();

        if (!GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(GlobalWarming.getInstance(), () -> {
            Material type = e.getLocation().getBlock().getType();

            if (Tag.LOGS.isTagged(type)) {
                double pollutionValue = GlobalWarming.getRegistry().getTreeGrowthAbsorption();

                if (pollutionValue > 0.0) {
                    PollutionManager.descendPollutionInWorld(world, pollutionValue);
                }
            }
        }, 2);
    }

    @EventHandler
    public void onPollutionChange(AsyncWorldPollutionChangeEvent e) {

        // This delayed task is needed to prevent multiple broadcasts
        Bukkit.getScheduler().runTaskLater(GlobalWarming.getInstance(), () -> {
            World world = e.getWorld();

            Long lastBroadcast = lastWorldBroadcasts.get(world.getName());
            if (lastBroadcast != null) {
                if ((System.currentTimeMillis() - lastBroadcast) < BROADCAST_COOLDOWN) {
                    return;
                }
            }
            lastWorldBroadcasts.put(world.getName(), System.currentTimeMillis());

            double amount = DoubleHandler.fixDouble(e.getNewValue() * GlobalWarming.getRegistry().getPollutionMultiply());
            if (!tempPollutionValues.containsKey(world.getName())) {
                tempPollutionValues.put(world.getName(), amount);
            }
            else {
                if (tempPollutionValues.get(world.getName()) == amount) {
                    return;
                }
            }

            tempPollutionValues.replace(world.getName(), amount);

            TemperatureType messageTempType = TemperatureType.valueOf(GlobalWarming.getMessages().getString("temperature-scale"));
            String difference = TemperatureUtils.getAirQualityString(world, messageTempType);

            String news = "";
            if (!GlobalWarming.getRegistry().getNews().isEmpty()) {
                String base = GlobalWarming.getMessages().getString("messages.breaking-news");
                List<String> newsList = GlobalWarming.getRegistry().getNews();
                String random = newsList.get(ThreadLocalRandom.current().nextInt(newsList.size()));

                news = ChatColors.color(base.replace("%news%", random));
            }

            for (Player p : world.getPlayers()) {
                p.sendMessage(ChatColors.color(GlobalWarming.getMessages().getString("messages.climate-change").replace("%value%", difference)));

                if (news.length() > 0) {
                    p.sendMessage(news);
                }
            }

        }, ThreadLocalRandom.current().nextInt(1, 20));
    }

    private boolean risePollutionTry(World world, String ID, ItemStack[] recipeInput) {
        double pollutionValue = calculatePollutionValue(ID, recipeInput);

        if (pollutionValue > 0.0) {
            PollutionManager.risePollutionInWorld(world, pollutionValue);
            return true;
        }

        return false;
    }

    private boolean descendPollutionTry(World world, String ID) {
        double absorptionValue = calculateAbsorptionValue(ID);

        if (absorptionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, absorptionValue);
            return true;
        }

        return false;
    }

    private double calculatePollutionValue(String ID, ItemStack[] recipeInput) {
        double pollutionValue = 0.0;
        
        pollutionValue += PollutionManager.isPollutedMachine(ID);

        for (ItemStack item : recipeInput) {
            pollutionValue += PollutionManager.isPollutedItem(item);
        }
        
        return pollutionValue;
    }

    private double calculateAbsorptionValue(String ID) {
        double absorptionValue = 0.0;

        absorptionValue += PollutionManager.isAbsorbentMachine(ID);

        return absorptionValue;
    }
}
