package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.utils.TemperatureUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class SlownessTask extends MechanicTask {

    private static ThreadLocalRandom rnd;

    public SlownessTask() {
        rnd = ThreadLocalRandom.current();
    }

    private void applyEffect(Player p, int amplifier) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, amplifier));
    }

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            if (GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && w.getPlayers().size() > 0) {
                for (Player p : w.getPlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.SLOW)) {
                        continue;
                    }

                    int rndInt = rnd.nextInt(10);

                    if (rndInt < 8) {
                        Temperature temp = TemperatureUtils.getTemperatureAtLocation(p.getLocation());
                        Double celsiusValue = temp.getCelsiusValue();
                        int amplifier = 0;

                        if (celsiusValue <= -30 || celsiusValue >= 50) {
                            amplifier = 3;
                        }
                        else if (celsiusValue <= -20 || celsiusValue >= 40) {
                            amplifier = 2;
                        }
                        else if (celsiusValue <= -10 || celsiusValue >= 36) {
                            amplifier = 1;
                        }
                        else {
                            continue;
                        }

                        applyEffect(p, amplifier);
                    }
                }
            }
        }
    }
}
