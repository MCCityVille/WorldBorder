package de.mccityville.worldborder.commands;

import de.mccityville.worldborder.WorldBorderPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BorderReloadCommand implements CommandExecutor {

    private final WorldBorderPlugin plugin;

    public BorderReloadCommand(WorldBorderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadBorderManager();
        sender.sendMessage(ChatColor.GREEN + "All Borders reloaded");
        return true;
    }
}
