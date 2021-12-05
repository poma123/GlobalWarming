package me.poma123.globalwarming.tasks;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.api.Temperature;

public class SlownessTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double chance;
    private final Research neededResearch;

    @ParametersAreNonnullByDefault
    public SlownessTask(double chance) {
        rnd = ThreadLocalRandom.current();
        this.chance = chance;
        neededResearch = GlobalWarmingPlugin.getRegistry().getResearchNeededForPlayerMechanics();
    }

    private void applyEffect(Player p, int duration, int amplifier) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) && w.getEnvironment() == World.Environment.NORMAL && !w.getPlayers().isEmpty()) {
                for (Player p : w.getPlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.SLOW)) {
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
                        int amplifier;
                        int duration;

                        if (celsiusValue <= -30 || celsiusValue >= 50) {
                            amplifier = 2;
                            duration = 100;
                        } else if (celsiusValue <= -20 || celsiusValue >= 40) {
                            amplifier = 1;
                            duration = 60;
                        } else if (celsiusValue <= -10 || celsiusValue >= 36) {
                            amplifier = 0;
                            duration = 40;
                        } else {
                            continue;
                        }

                        applyEffect(p, duration, amplifier);
                    }
                }
            }
        }
    }
}
