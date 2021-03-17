package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiversePortals.event.MVPortalEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PortalListener implements Listener {

    private final MultiverseCommandDestination plugin;

    public PortalListener(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortal(MVPortalEvent event) {
        if (!(event.getDestination() instanceof CommandDestination)) {
            return;
        }
        CommandDestination cmdDest = (CommandDestination) event.getDestination();
        this.plugin.runCommand(event.getTeleportee(), cmdDest.getName());

        event.setCancelled(true);
    }
}
