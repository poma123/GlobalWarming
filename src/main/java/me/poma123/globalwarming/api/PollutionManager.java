package me.poma123.globalwarming.api;

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

/**
 * A very useful API that handles the pollution amount in {@link World} instances
 * and the pollution production of items and machines.
 *
 * @author poma123
 *
 */
public class PollutionManager {

    public static double getPollutionAtLocation(@Nonnull Location loc) {
        return getPollutionInWorld(loc.getWorld());
    }

    /**
     * This returns the pollution amount in a {@link World}
     *
     * @param world
     *            The {@link World} to get the pollution amount from
     *
     * @return the pollution amount in the given {@link World}
     */
    public static double getPollutionInWorld(@Nonnull World world) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                return config.getDouble("data.pollution");
            }
        }
        return 0.0;
    }

    /**
     * This increases the pollution amount in a {@link World}
     *
     * @param world
     *            The {@link World} to set pollution amount
     * @param value
     *            The value to rise
     *
     * @return whether the change was successful
     */
    public static boolean risePollutionInWorld(@Nonnull World world, @Nonnull double value) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                double oldValue = config.getDouble("data.pollution");
                value = oldValue + value;

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, value);
                Bukkit.getScheduler().runTaskAsynchronously(GlobalWarming.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));

                config.setValue("data.pollution", value);
                config.save();
                return true;
            }
        }
        return false;
    }

    /**
     * This decreases the pollution amount in a {@link World}
     *
     * @param world
     *            The {@link World} to set pollution amount
     * @param value
     *            The value to descend
     *
     * @return whether the change was successful
     */
    public static boolean descendPollutionInWorld(@Nonnull World world, @Nonnull double value) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                double oldValue = config.getDouble("data.pollution");
                value = Math.max(oldValue - value, 0.0);

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, value);
                Bukkit.getScheduler().runTaskAsynchronously(GlobalWarming.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));

                config.setValue("data.pollution", value);
                config.save();
                return true;
            }
        }
        return false;
    }

    /**
     * This sets the pollution amount in a {@link World}
     *
     * @param world
     *            The {@link World} to set pollution amount
     * @param newValue
     *            The value to set
     *
     * @return whether the change was successful
     */
    public static boolean setPollutionInWorld(@Nonnull World world, @Nonnull double newValue) {
        if (GlobalWarming.getRegistry().isWorldEnabled(world.getName())) {
            Config config = GlobalWarming.getRegistry().getWorldConfig(world);

            if (config != null) {
                double oldValue = config.getDouble("data.pollution");

                AsyncWorldPollutionChangeEvent event = new AsyncWorldPollutionChangeEvent(world, oldValue, newValue);
                Bukkit.getScheduler().runTaskAsynchronously(GlobalWarming.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));

                config.setValue("data.pollution", newValue);
                config.save();
                return true;
            }
        }
        return false;
    }

    /**
     * This returns the pollution production of a {@link ItemStack}
     *
     * @param item
     *            The {@link ItemStack} to check
     *
     * @return the pollution production of the {@link ItemStack}
     */
    public static double isPollutedItem(@Nonnull ItemStack item) {
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

    /**
     * This returns the pollution production of a {@link SlimefunItem} machine
     *
     * @param ID
     *            The ID of the {@link SlimefunItem} machine
     *
     * @return the pollution production of the {@link SlimefunItem} machine
     */
    public static double isPollutedMachine(@Nonnull String ID) {
        SlimefunItem sfItem = SlimefunItem.getByID(ID);
        Map<String, Double> pollutedSlimefunMachines = GlobalWarming.getRegistry().getPollutedSlimefunMachines();

        if (sfItem != null && pollutedSlimefunMachines.containsKey(sfItem.getID())) {
            return pollutedSlimefunMachines.get(sfItem.getID());
        }

        return 0.0;
    }

    /**
     * This returns the pollution absorbtion of a {@link SlimefunItem} machine
     *
     * @param ID
     *            The ID of the {@link SlimefunItem} machine
     *
     * @return the pollution absorbtion of the {@link SlimefunItem} machine
     */
    public static double isAbsorbentMachine(@Nonnull String ID) {
        SlimefunItem sfItem = SlimefunItem.getByID(ID);
        Map<String, Double> absorbentSlimefunMachines = GlobalWarming.getRegistry().getAbsorbentSlimefunMachines();

        if (sfItem != null && absorbentSlimefunMachines.containsKey(sfItem.getID())) {
            return absorbentSlimefunMachines.get(sfItem.getID());
        }

        return 0.0;
    }
}
