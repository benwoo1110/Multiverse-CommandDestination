package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.event.MVConfigReloadEvent;
import com.onarandombox.MultiverseCore.event.MVVersionEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class CoreListener {

    public static void registerEvents(MultiverseCommandDestination plugin) {
        try {
            Class.forName("com.onarandombox.MultiverseCore.event.MVConfigReloadEvent");
            Bukkit.getPluginManager().registerEvents(new ReloadListener(plugin), plugin);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.onarandombox.MultiverseCore.event.MVVersionEvent");
            Bukkit.getPluginManager().registerEvents(new VersionListener(plugin), plugin);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class ReloadListener implements Listener {
        private final MultiverseCommandDestination plugin;

        public ReloadListener(MultiverseCommandDestination plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onReload(MVConfigReloadEvent event) {
            this.plugin.loadConfig();
            event.addConfig("Multiverse-CommandDestination - config.yml");
        }
    }

    static class VersionListener implements Listener {
        private final MultiverseCommandDestination plugin;

        public VersionListener(MultiverseCommandDestination plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onVersion(MVVersionEvent event) {
            event.putDetailedVersionInfo(
                    "Multiverse-CommandDestination/config.yml",
                    new File(this.plugin.getDataFolder(), "config.yml")
            );
            event.appendVersionInfo(
                    "[Multiverse-CommandDestination] Loaded commands: " + this.plugin.getCommandMap() + "\n" +
                            "[Multiverse-CommandDestination] Do papi hook: " + this.plugin.isDoPapiHook() + "\n" +
                            "[Multiverse-CommandDestination] MV-Core installed: " + (this.plugin.getCore() != null) + "\n" +
                            "[Multiverse-CommandDestination] MV-Portals installed: " + (this.plugin.getPortals() != null) + "\n" +
                            "[Multiverse-CommandDestination] Papi installed: " + (this.plugin.getPapi() != null)
            );
        }
    }
}
