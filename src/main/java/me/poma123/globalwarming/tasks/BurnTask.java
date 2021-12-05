package me.poma123.globalwarming.tasks;


import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.api.Temperature;

public class BurnTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double chance;
    private final Research neededResearch;

    @ParametersAreNonnullByDefault
    public BurnTask(double chance) {
        rnd = ThreadLocalRandom.current();
        this.chance = chance;
        neededResearch = GlobalWarmingPlugin.getRegistry().getResearchNeededForPlayerMechanics();
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) && w.getEnvironment() == World.Environment.NORMAL && !w.getPlayers().isEmpty()) {
                for (Player p : w.getPlayers()) {
                    if (p.getFireTicks() > 0) {
                        continue;
                    }

                    if (neededResearch != null) {
                        Optional<PlayerProfile> profile = PlayerProfile.find(p);

                        if (profile.isPresent() && !profile.get().hasUnlocked(neededResearch)) {
                            continue;
                        }
                    }

                    double random = rnd.nextDouble();

                    if (random < chance) {
                        Temperature temp = GlobalWarmingPlugin.getTemperatureManager().getTemperatureAtLocation(p.getLocation());
                        double celsiusValue = temp.getCelsiusValue();

                        if (celsiusValue >= 50) {
                            p.setFireTicks(30);
                        } else if (celsiusValue >= 60){
                            p.setFireTicks(80);
                        }
                    }
                }
            }
        }
    }
}
