package fr.mineral.Commands.CVAR;

import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_enable_old_pvp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("mp_enable_old_pvp")) {
                if(sender.isOp()) {
                    if(args.length == 1) {
                        int valeur = 1;
                        try {
                            valeur = Integer.parseInt(args[0]) % 2;
                            GameSettingsCvar.mp_enable_old_pvp.setValue(valeur);
                            if(valeur == 1) {
                                mineralcontest.broadcastMessage(mineralcontest.prefixPrive + Lang.cvar_old_pvp_enabled.toString());
                                for(Player onlinePlayers : player.getWorld().getPlayers())
                                    onlinePlayers.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024d);
                            }
                            else {
                                mineralcontest.broadcastMessage(mineralcontest.prefixPrive + Lang.cvar_old_pvp_disabled.toString());
                                for(Player onlinePlayers : player.getWorld().getPlayers())
                                    onlinePlayers.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4d);
                            }



                            return false;
                        }catch (NumberFormatException nfe){
                            nfe.printStackTrace();
                            sender.sendMessage("Usage: /mp_enable_old_pvp <0=DISABLED, 1=ENABLED>");
                            return false;
                        }

                    } else {
                        sender.sendMessage("Usage: /mp_enable_old_pvp <0=DISABLED, 1=ENABLED>");
                        return false;
                    }

                }
            }

            return false;
        }

        return false;
    }
}