package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerChangedWorldEvent;
import fr.synchroneyes.custom_events.MCPlayerLeaveWorldPluginEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerWorldChangeEvent implements Listener {


    /**
     * Méthode appelée lorsqu'un joueur du plugin change de monde
     * @param event
     */
    @EventHandler
    public void onPlayerWorldChangeEvent(MCPlayerChangedWorldEvent event) {
        // On regarde la destination
        // Si on va dans le nether ou dans l'end, on cancel l'event
        if(event.getTo().getEnvironment() != World.Environment.NORMAL) {
            event.setCancelled(true);
            event.getJoueur().sendPrivateMessage(mineralcontest.prefixErreur + "L'accès à ce monde n'est pas autorisé. Vous devez rester dans le monde normal et non dans le monde: " + event.getTo().getEnvironment().name());
            return;
        }

        // On regarde si il quitte le monde
        // Si le joueur va dans un monde qui ne fait plus partie du plugin
        if(!mineralcontest.isAMineralContestWorld(event.getTo())) {
            // Le joueur quitte le "plugin"
            MCPlayerLeaveWorldPluginEvent event1 = new MCPlayerLeaveWorldPluginEvent(event.getJoueur().getJoueur());
            Bukkit.getServer().getPluginManager().callEvent(event1);
            return;
        }else {
            // Le joueur va rejoindre le plugin à nouveau
            event.getJoueur().sendPrivateMessage("join plugin à nouveau");

        }



    }

}
