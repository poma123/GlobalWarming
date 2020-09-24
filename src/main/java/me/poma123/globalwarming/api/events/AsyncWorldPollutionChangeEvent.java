package me.poma123.globalwarming.api.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.ParametersAreNonnullByDefault;

public class AsyncWorldPollutionChangeEvent extends Event {
    public static final HandlerList handlerList = new HandlerList();

    private final World world;
    private final double oldValue;
    private final double newValue;

    @ParametersAreNonnullByDefault
    public AsyncWorldPollutionChangeEvent(World world, double oldValue, double newValue) {
        super(true);

        this.world = world;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public World getWorld() {
        return world;
    }

    public double getOldValue() {
        return oldValue;
    }

    public double getNewValue() {
        return newValue;
    }
}