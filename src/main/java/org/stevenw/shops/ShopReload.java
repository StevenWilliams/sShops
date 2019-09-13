package org.stevenw.shops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShopReload implements CommandExecutor {
    private sShops plugin;

    public ShopReload(sShops plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        commandSender.sendMessage("Reloading sShops config");
        plugin.reloadConfig();
        commandSender.sendMessage("sShops config reloaded.");
        return true;
    }
}
