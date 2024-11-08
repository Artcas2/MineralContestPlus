package fr.synchroneyes.mineral.Core.Referee.Items;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.synchroneyes.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ChatColorString;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TeleportToPlayerItem extends RefereeItemTemplate {
    public TeleportToPlayerItem(Object target, InventoryTemplate inventaireSource) {
        super(target, inventaireSource);
    }

    @Override
    public void performClick(Player joueur) {
        if (this.target instanceof Player) {
            joueur.sendMessage(Lang.referee_item_teleporting_you_to_player.toString());
            joueur.teleport((Entity)((Player)this.target));
        }
    }

    @Override
    public String getNomItem() {
        return Lang.referee_item_teleport_to_player_title.toString() + ((Player)this.target).getDisplayName();
    }

    @Override
    public String getDescriptionItem() {
        Player playerToTeleportName = (Player)this.target;
        return Lang.referee_item_teleport_to_player_description.toString() + playerToTeleportName.getDisplayName();
    }

    @Override
    public Material getItemMaterial() {
        Player playerToTeleportName = (Player)this.target;
        Game playerGame = mineralcontest.getPlayerGame(playerToTeleportName);
        if (playerGame == null || playerGame.getPlayerHouse(playerToTeleportName) == null) {
            return Material.WHITE_WOOL;
        }
        try {
            return Material.valueOf((String)(ChatColorString.toStringEN(playerGame.getPlayerTeam(playerToTeleportName).getCouleur()) + "_CONCRETE"));
        } catch (IllegalArgumentException iae) {
            return Material.WHITE_WOOL;
        }
    }
}

