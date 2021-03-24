package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeleportListener implements Listener {

    private final MultiverseCommandDestination plugin;

    public TeleportListener(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(MVTeleportEvent event) {
        if (!(event.getDestination() instanceof CommandDestination)) {
            return;
        }
        CommandDestination cmdDest = (CommandDestination) event.getDestination();
        this.plugin.runCommand(event.getTeleportee(), cmdDest.getName());

        event.setCancelled(true);
    }
}
