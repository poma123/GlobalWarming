package me.poma123.globalwarming.tasks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.utils.TemperatureUtils;

public class SlownessTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double chance;

    @ParametersAreNonnullByDefault
    public SlownessTask(double chance) {
        rnd = ThreadLocalRandom.current();
        this.chance = chance;
    }

    private void applyEffect(Player p, int duration, int amplifier) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarming.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && w.getPlayers().size() > 0) {
                for (Player p : w.getPlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.SLOW)) {
                        continue;
                    }

                    double random = rnd.nextDouble();

                    if (random < chance) {
                        Temperature temp = TemperatureUtils.getTemperatureAtLocation(p.getLocation());
                        double celsiusValue = temp.getCelsiusValue();
                        int amplifier;
                        int duration;

                        if (celsiusValue <= -30 || celsiusValue >= 50) {
                            amplifier = 2;
                            duration = 100;
                        }
                        else if (celsiusValue <= -20 || celsiusValue >= 40) {
                            amplifier = 1;
                            duration = 60;
                        }
                        else if (celsiusValue <= -10 || celsiusValue >= 36) {
                            amplifier = 0;
                            duration = 40;
                        }
                        else {
                            continue;
                        }

                        applyEffect(p, duration, amplifier);
                    }
                }
            }
        }
    }
}
