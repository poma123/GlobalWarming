package me.poma123.globalwarming.utils;

import javax.annotation.Nonnull;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.api.events.AsyncWorldPollutionChangeEvent;

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

    public static double isPollutedItem(ItemStack item) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        Map<String, Double> pollutedSlimefunItems = GlobalWarming.getRegistry().getPollutedSlimefunItems();
        Map<Material, Double> pollutedVanillaItems = GlobalWarming.getRegistry().getPollutedVanillaItems();

        if (sfItem != null && pollutedSlimefunItems.containsKey(sfItem.getID())) {
            return pollutedSlimefunItems.get(sfItem.getID());
        }

        if (pollutedVanillaItems.containsKey(item.getType())) {
            return pollutedVanillaItems.get(item.getType());
        }

        return 0.0;
    }
}
