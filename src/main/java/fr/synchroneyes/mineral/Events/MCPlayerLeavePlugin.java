package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerLeavePluginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MCPlayerLeavePlugin implements Listener {
    @EventHandler
    public void onPlayerDisconnect(MCPlayerLeavePluginEvent event) {
        event.getMcPlayer().disconnectPlayer();
    }
}

