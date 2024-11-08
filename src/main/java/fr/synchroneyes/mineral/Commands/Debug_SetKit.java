package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Kits.Classes.Agile;
import fr.synchroneyes.mineral.Kits.Classes.Enchanteur;
import fr.synchroneyes.mineral.Kits.Classes.Guerrier;
import fr.synchroneyes.mineral.Kits.Classes.Mineur;
import fr.synchroneyes.mineral.Kits.Classes.Parieur;
import fr.synchroneyes.mineral.Kits.Classes.Robuste;
import fr.synchroneyes.mineral.Kits.Classes.Soutien;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Debug_SetKit extends CommandTemplate {
    public Debug_SetKit() {
        this.accessCommande.add(4);
        this.accessCommande.add(0);
        this.accessCommande.add(14);
        this.addArgument("Classe", false);
    }

    @Override
    public String getCommand() {
        return "mckit";
    }

    @Override
    public String getDescription() {
        return "Permet de devenir guerrier";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player)commandSender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
        if (args.length > 0) {
            joueur.sendMessage("Vous allez devenir: " + args[0]);
            switch (args[0]) {
                case "mineur": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Mineur());
                    break;
                }
                case "guerrier": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Guerrier());
                    break;
                }
                case "agile": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Agile());
                    break;
                }
                case "enchanteur": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Enchanteur());
                    break;
                }
                case "robuste": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Robuste());
                    break;
                }
                case "soutien": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Soutien());
                    break;
                }
                case "parieur": {
                    playerGroup.getKitManager().setPlayerKit(joueur, new Parieur());
                    break;
                }
                default: {
                    joueur.sendMessage("Ce kit n'est pas reconnu...");
                    break;
                }
            }
        } else {
            playerGroup.getKitManager().openInventoryToPlayer(joueur);
        }
        return false;
    }
}

