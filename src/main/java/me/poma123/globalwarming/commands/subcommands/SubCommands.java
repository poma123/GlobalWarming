package me.poma123.globalwarming.commands.subcommands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import me.poma123.globalwarming.commands.SubCommand;
import me.poma123.globalwarming.GlobalWarming;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;

public class SubCommands {
    
    private SubCommands() {}

    public static Collection<SubCommand> getAllCommands(GlobalWarmingCommand cmd) {
        GlobalWarming plugin = cmd.getPlugin();
        List<SubCommand> commands = new LinkedList<>();

        commands.add(new PollutionCommand(plugin, cmd));

        return commands;
    }
}
