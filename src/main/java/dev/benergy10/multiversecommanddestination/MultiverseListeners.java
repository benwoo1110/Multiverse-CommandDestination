package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.api.MVDestination;
import com.onarandombox.MultiverseCore.event.MVConfigReloadEvent;
import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import com.onarandombox.MultiverseCore.event.MVVersionEvent;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MultiverseListeners {

    public static void registerEvents(MultiverseCommandDestination plugin) {
        tryRegister("com.onarandombox.MultiverseCore.event.MVTeleportEvent", new TeleportListener(plugin));
        plugin.getLogger().info("§aThe following message is safe to ignore:");
        tryRegister("com.onarandombox.MultiversePortals.event.MVPortalEvent", new PortalListener(plugin));
        tryRegister("com.onarandombox.MultiverseCore.event.MVConfigReloadEvent", new ReloadListener(plugin));
        tryRegister("com.onarandombox.MultiverseCore.event.MVVersionEvent", new VersionListener(plugin));
    }

    private static void tryRegister(String eventClass, AbstractListener listener) {
        try {
            Class.forName(eventClass);
            Bukkit.getPluginManager().registerEvents(listener, listener.plugin);
        } catch (ClassNotFoundException e) {
            // ignore
        }
    }

    private static abstract class AbstractListener implements Listener {
        protected final MultiverseCommandDestination plugin;
        private AbstractListener(MultiverseCommandDestination plugin) {
            this.plugin = plugin;
        }
    }

    private static class TeleportListener extends AbstractListener {
        private TeleportListener(MultiverseCommandDestination plugin) {
            super(plugin);
        }
        @EventHandler
        public void onTeleport(MVTeleportEvent event) {
            MVDestination dest = event.getDestination();
            if (!(dest instanceof CommandDestination)) {
                return;
            }
            this.plugin.runCommand(event.getTeleporter(), event.getTeleportee(), dest.getName());
            event.setCancelled(true);
        }
    }

    private static class PortalListener extends AbstractListener {
        private PortalListener(MultiverseCommandDestination plugin) {
            super(plugin);
        }
        @EventHandler
        public void onPortal(MVPortalEvent event) {
            MVDestination dest = event.getDestination();
            if (!(dest instanceof CommandDestination)) {
                return;
            }
            this.plugin.runCommand(event.getTeleportee(), event.getTeleportee(), dest.getName());
            event.setCancelled(true);
        }
    }

    private static class ReloadListener extends AbstractListener {
        private ReloadListener(MultiverseCommandDestination plugin) {
            super(plugin);
        }
        @EventHandler
        public void onReload(MVConfigReloadEvent event) {
            this.plugin.loadConfig();
            event.addConfig("Multiverse-CommandDestination - config.yml");
        }
    }

    private static class VersionListener extends AbstractListener {
        private VersionListener(MultiverseCommandDestination plugin) {
            super(plugin);
        }
        @EventHandler
        public void onVersion(MVVersionEvent event) {
            event.putDetailedVersionInfo("Multiverse-CommandDestination/config.yml", this.plugin.getConfigFile());
            event.appendVersionInfo(
                    "[Multiverse-CommandDestination] Multiverse-CommandDestination version: " + this.plugin.getDescription().getVersion() + "\n" +
                            "[Multiverse-CommandDestination] Loaded commands: " + this.plugin.getCommandMap() + "\n" +
                            "[Multiverse-CommandDestination] Do papi hook: " + this.plugin.isDoPapiHook() + "\n"
            );
        }
    }
}
