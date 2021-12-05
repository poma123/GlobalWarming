package me.poma123.globalwarming.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This {@link Event} is fired whenever the pollution level has changed in a {@link World}.
 *
 * @author poma123
 *
 */
public class AsyncWorldPollutionChangeEvent extends Event {
    public static final HandlerList handlers = new HandlerList();

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

    /**
     * This method returns the {@link World} where the change has happened
     *
     * @return the {@link World} where the change has happened
     */
    @Nonnull
    public World getWorld() {
        return world;
    }

    /**
     * This method returns the pollution amount before the change
     *
     * @return the pollution amount before the change
     */
    @Nonnull
    public double getOldValue() {
        return oldValue;
    }

    /**
     * This method returns the pollution amount after the change
     *
     * @return the pollution amount after the change
     */
    @Nonnull
    public double getNewValue() {
        return newValue;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}