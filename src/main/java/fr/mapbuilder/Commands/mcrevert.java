package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.MapBuilder;
import fr.mineral.Utils.BlockSaver;
import fr.mineral.Utils.ChatColorString;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Stack;

public class mcrevert extends CommandTemplate {
    @Override
    public String getCommand() {
        return "mcrevert";
    }


    @Override
    public String getDescription() {
        return "Permet";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        if (!MapBuilder.modifications.empty()) {
            Stack<BlockSaver> dernierPose = MapBuilder.modifications.pop();
            for (BlockSaver block : dernierPose)
                block.applyMethod();
            commandSender.sendMessage(mineralcontest.prefixPrive + ChatColor.GREEN + "Revert done!");
        } else {
            commandSender.sendMessage(mineralcontest.prefixPrive + ChatColor.RED + "Nothing to revert!");

        }
        return false;
    }
}
