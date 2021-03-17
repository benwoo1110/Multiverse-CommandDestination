package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.event.MVConfigReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoreListener implements Listener {

    private final MultiverseCommandDestination plugin;

    public CoreListener(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(MVConfigReloadEvent event) {
        this.plugin.loadConfig();
        event.addConfig("Multiverse-CommandDestination - config.yml");
    }
}
