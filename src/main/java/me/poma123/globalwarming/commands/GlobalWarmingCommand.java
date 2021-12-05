package me.poma123.globalwarming.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.commands.subcommands.SubCommands;

public class GlobalWarmingCommand implements CommandExecutor, Listener {

    private boolean registered = false;
    private final GlobalWarmingPlugin plugin;
    private final List<SubCommand> commands = new LinkedList<>();

    public GlobalWarmingCommand(@Nonnull GlobalWarmingPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Validate.isTrue(!registered, "GlobalWarming's subcommands have already been registered!");

        registered = true;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getCommand("globalwarming").setExecutor(this);
        commands.addAll(SubCommands.getAllCommands(this));
    }

    @Nonnull
    public GlobalWarmingPlugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand command : commands) {
                if (args[0].equalsIgnoreCase(command.getName())) {
                    command.onExecute(sender, args);
                    return true;
                }
            }
        }

        sendHelp(sender);

        // We could just return true here, but if there's no subcommands, then
        // something went horribly wrong anyway. This will also stop sonarcloud
        // from nagging about this always returning true...
        return !commands.isEmpty();
    }

    public void sendHelp(@Nonnull CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&aGlobalWarming &2v" + Slimefun.getVersion()));
        sender.sendMessage("");

        for (SubCommand cmd : commands) {
            if (!cmd.isHidden()) {
                sender.sendMessage(ChatColors.color("&3/globalwarming " + cmd.getName() + " &b") + cmd.getDescription());
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/help globalwarming")) {
            sendHelp(e.getPlayer());
            e.setCancelled(true);
        }
    }

    /**
     * This returns A {@link List} containing every possible {@link SubCommand} of this {@link Command}.
     *
     * @return A {@link List} containing every {@link SubCommand}
     */
    @Nonnull
    public List<String> getSubCommandNames() {
        return commands.stream().map(SubCommand::getName).collect(Collectors.toList());
    }

}