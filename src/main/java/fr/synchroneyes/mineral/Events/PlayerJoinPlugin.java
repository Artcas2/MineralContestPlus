package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Utils.DisconnectedPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


/**
 * Classe gérant l'arrivé d'un joueur dans le plugin
 */
public class PlayerJoinPlugin implements Listener {

    @EventHandler
    public void onPlayerJoinPlugin(MCPlayerJoinEvent event) {
        // On commence par récuperer le joueur
        Player joueur = event.getPlayer();

        Bukkit.broadcastMessage("Le joueur " + joueur.getDisplayName() + " s'est connecté au serveur");

        // On regarde si le joueur s'est déconnecté avant du plugin
        DisconnectedPlayer disconnectedPlayer = mineralcontest.plugin.wasPlayerDisconnected(joueur);

        Bukkit.broadcastMessage("Le joueur faisait parti du plugin? " + (disconnectedPlayer != null));
        // SI le joueur faisait parti du plugin
        if (disconnectedPlayer != null) {

            Bukkit.broadcastMessage("Le joueur faisait parti du plugin");

            // On le reconnecte
            mineralcontest.plugin.addNewPlayer(joueur);

            // On récupère son instance
            MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

            // Si MCPlayer est null; il ne s'était jamais connecté
            if(mcPlayer == null){
                Bukkit.broadcastMessage("Le joueur ne s'était jamais connecté");
                return;
            }

            // On remet ses informations
            mcPlayer.reconnectPlayer(disconnectedPlayer);


            return;
        }

        // On l'ajoute au plugin
        mineralcontest.plugin.addNewPlayer(joueur);
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

        // On regarde si on est en version communautaire ou non
        if(!mineralcontest.communityVersion) {
            // On l'ajoute au groupe par défault
            if(joueur.isOp()) mineralcontest.plugin.getNonCommunityGroup().addAdmin(joueur);
            else mineralcontest.plugin.getNonCommunityGroup().addJoueur(joueur);

            // Si la game avait démarré, on le met spec
            if(mcPlayer.getGroupe() != null) {
                if(mcPlayer.getGroupe().getGame().isGameStarted()) {
                    joueur.setGameMode(GameMode.SPECTATOR);
                    Bukkit.broadcastMessage("Le joueur a été mis en spectateur !");
                }
            }
        }
    }
}