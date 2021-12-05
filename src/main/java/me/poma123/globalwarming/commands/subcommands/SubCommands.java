package me.poma123.globalwarming.commands.subcommands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;
import me.poma123.globalwarming.commands.SubCommand;

public class SubCommands {
    
    private SubCommands() {}

    public static Collection<SubCommand> getAllCommands(GlobalWarmingCommand cmd) {
        GlobalWarmingPlugin plugin = cmd.getPlugin();
        List<SubCommand> commands = new LinkedList<>();

        commands.add(new PollutionCommand(plugin, cmd));

        return commands;
    }
}
