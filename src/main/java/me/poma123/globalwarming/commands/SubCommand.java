package me.poma123.globalwarming.commands;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import me.poma123.globalwarming.GlobalWarmingPlugin;

public abstract class SubCommand {

    protected final GlobalWarmingPlugin plugin;
    protected final GlobalWarmingCommand cmd;

    private final String name;
    private final String description;
    private final boolean hidden;

    @ParametersAreNonnullByDefault
    protected SubCommand(GlobalWarmingPlugin plugin, GlobalWarmingCommand cmd, String name, String description, boolean hidden) {
        this.plugin = plugin;
        this.cmd = cmd;

        this.name = name;
        this.description = description;
        this.hidden = hidden;
    }

    @Nonnull
    public final String getName() {
        return name;
    }

    public final boolean isHidden() {
        return hidden;
    }

    protected void recordUsage(@Nonnull Map<SubCommand, Integer> commandUsage) {
        commandUsage.merge(this, 1, Integer::sum);
    }

    public abstract void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args);

    @Nonnull
    protected String getDescription() {
        return description;
    }

}