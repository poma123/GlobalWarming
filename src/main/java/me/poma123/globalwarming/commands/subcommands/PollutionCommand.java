package me.poma123.globalwarming.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.TemperatureManager;
import me.poma123.globalwarming.api.PollutionManager;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;
import me.poma123.globalwarming.commands.SubCommand;

class PollutionCommand extends SubCommand {

    PollutionCommand(GlobalWarmingPlugin plugin, GlobalWarmingCommand cmd) {
        super(plugin, cmd, "pollution", "Allows you to manually change the pollution amount", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("globalwarming.command.pollution") || !(sender instanceof Player)) {
            if (args.length > 2) {
                World world = Bukkit.getWorld(args[2]);

                if (world != null && GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
                    if (args[1].equalsIgnoreCase("get")) {
                        double pollution = TemperatureManager.fixDouble(PollutionManager.getPollutionInWorld(world), 2);

                        sender.sendMessage(ChatColors.color("&bPollution amount in world &a" + world.getName() + "&b: &a" + pollution));
                    } else if (args[1].equalsIgnoreCase("set")) {
                        if (args.length > 3) {
                            setPollution(sender, world, args);
                        } else {
                            sender.sendMessage(ChatColors.color("&4Usage: &c/globalwarming pollution <set> <world> <amount>"));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColors.color("&4The plugin's functionality is disabled in the given world"));
                }
            } else {
                sender.sendMessage(ChatColors.color("&4Usage: &c/globalwarming pollution <set|get> <world>"));
            }
        } else {
            sender.sendMessage(ChatColors.color("&4You do not have the required permission to do this"));
        }
    }

    private void setPollution(CommandSender sender, World world, String[] args) {
        int amount = parseAmount(args);

        if (amount > -1) {
            if (PollutionManager.setPollutionInWorld(world, amount)) {
                sender.sendMessage(ChatColors.color("&bYou have changed the pollution value to '&a%newValue%&b' in world '&a%world%&b'").replace("%newValue%", amount + "").replace("%world%", world.getName()));
            } else {
                // This is nearly impossible, but let us check
                sender.sendMessage(ChatColors.color("&4The plugin's functionality is disabled in the given world"));
            }
        } else {
            sender.sendMessage(ChatColors.color("&4%amount% &cis not a valid amount").replace("%amount%", amount + ""));
        }
    }

    private int parseAmount(String[] args) {
        int amount = -1;

        if (args.length == 4 && PatternUtils.NUMERIC.matcher(args[3]).matches()) {
            amount = Integer.parseInt(args[3]);
        }

        return amount;
    }
}
