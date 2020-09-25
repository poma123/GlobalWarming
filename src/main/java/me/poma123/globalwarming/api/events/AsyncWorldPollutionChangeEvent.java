package me.poma123.globalwarming.api.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is fired whenever the pollution level has changed in a {@link World}.
 *
 * @author poma123
 *
 */
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

    @Nonnull
    public World getWorld() {
        return world;
    }

    @Nonnull
    public double getOldValue() {
        return oldValue;
    }

    @Nonnull
    public double getNewValue() {
        return newValue;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}