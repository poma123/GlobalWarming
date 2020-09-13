package me.poma123.globalwarming.api.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldPollutionChangeEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private World world;

    public WorldPollutionChangeEvent(World world) {
        this.world = world;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}