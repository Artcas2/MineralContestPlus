package fr.synchroneyes.mineral.Core.Referee.Items;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetInvisibleItem extends RefereeItemTemplate {
    public SetInvisibleItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);
        if (playerGroupe == null) {
            return;
        }
        boolean playerHidden = PlayerUtils.isPlayerHidden(joueur);
        if (playerHidden) {
            joueur.sendMessage(mineralcontest.prefixPrive + "Vous \u00eates maintenant visible");
        } else {
            joueur.sendMessage(mineralcontest.prefixPrive + "Vous \u00eates maintenant invisible");
        }
        for (Player membre_groupe : playerGroupe.getPlayers()) {
            if (playerGroupe.getGame().isReferee(membre_groupe)) continue;
            if (playerHidden) {
                membre_groupe.showPlayer((Plugin)mineralcontest.plugin, joueur);
                continue;
            }
            membre_groupe.hidePlayer((Plugin)mineralcontest.plugin, joueur);
        }
    }

    @Override
    public String getNomItem() {
        return "Se rendre visible/invisible";
    }

    @Override
    public String getDescriptionItem() {
        return "Permet de se rendre visible ou non par les autres joueurs";
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_BANNER;
    }
}

