package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.utils.TemperatureUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BurnTask extends MechanicTask {

    private final ThreadLocalRandom rnd;
    private final double chance;

    @ParametersAreNonnullByDefault
    public BurnTask(double chance) {
        rnd = ThreadLocalRandom.current();
        this.chance = chance;
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarming.getRegistry().getEnabledWorlds();

        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld(worldName);

            if (w != null && GlobalWarming.getRegistry().isWorldEnabled(w.getName()) && w.getPlayers().size() > 0) {
                for (Player p : w.getPlayers()) {
                    if (p.getFireTicks() > 0) {
                        continue;
                    }

                    double random = rnd.nextDouble();

                    if (random < chance) {
                        Temperature temp = TemperatureUtils.getTemperatureAtLocation(p.getLocation());
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
