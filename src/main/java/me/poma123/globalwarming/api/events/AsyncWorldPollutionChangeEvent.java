package me.poma123.globalwarming.api.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.ParametersAreNonnullByDefault;

public class AsyncWorldPollutionChangeEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private World world;
    private Double oldValue;
    private Double newValue;

    @ParametersAreNonnullByDefault
    public AsyncWorldPollutionChangeEvent(World world, Double oldValue, Double newValue) {
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
}