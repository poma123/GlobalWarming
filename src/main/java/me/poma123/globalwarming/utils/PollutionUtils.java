package me.poma123.globalwarming.utils;

import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.events.AsyncWorldPollutionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class PollutionUtils {

    public static double getPollutionAtLocation(@Nonnull Location loc) {
        return getPollutionInWorld(loc.getWorld());
    }

    public static double getPollutionInWorld(@Nonnull World world) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                return config.getDouble("data.pollution");
            }
        }
        return 0.0;
    }

    public static boolean risePollutionInWorld(@Nonnull World world, Double value) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                Double oldValue = config.getDouble("data.pollution");
                value = oldValue + value;

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, value);
                Bukkit.getPluginManager().callEvent(event);

                config.setValue("data.pollution", value);
                config.save();
                return true;
            }
        }
        return false;
    }

    public static boolean descendPollutionInWorld(@Nonnull World world, Double value) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                Double oldValue = config.getDouble("data.pollution");
                value = Math.max(oldValue - value, 0.0);

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, value);
                Bukkit.getPluginManager().callEvent(event);

                config.setValue("data.pollution", value);
                config.save();
                return true;
            }
        }
        return false;
    }

    public static boolean setPollutionInWorld(@Nonnull World world, Double newValue) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                Double oldValue = config.getDouble("data.pollution");

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, newValue);
                Bukkit.getPluginManager().callEvent(event);

                config.setValue("data.pollution", newValue);
                config.save();
                return true;
            }
        }
        return false;
    }
}
