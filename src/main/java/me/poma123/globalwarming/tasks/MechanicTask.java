package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import org.bukkit.Bukkit;

public abstract class MechanicTask implements Runnable {

    protected int id;

    public void setID(int id) {
        this.id = id;
    }

    public void schedule(long delay) {
        setID(Bukkit.getScheduler().scheduleSyncDelayedTask(GlobalWarmingPlugin.getInstance(), this, delay));
    }

    public void scheduleRepeating(long delay, long interval) {
        setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(GlobalWarmingPlugin.getInstance(), this, delay, interval));
    }

    public void scheduleAsyncRepeating(long delay, long interval) {
        setID(Bukkit.getScheduler().scheduleAsyncRepeatingTask(GlobalWarmingPlugin.getInstance(), this, delay, interval));
    }

}
