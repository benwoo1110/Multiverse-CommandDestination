package dev.benergy10.multiversecommanddestination;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mvplugins.multiverse.core.destination.DestinationInstance;
import org.mvplugins.multiverse.core.event.MVConfigReloadEvent;
import org.mvplugins.multiverse.core.event.MVDumpsDebugInfoEvent;
import org.mvplugins.multiverse.core.event.MVTeleportDestinationEvent;
import org.mvplugins.multiverse.portals.event.MVPortalEvent;

public class MultiverseListeners {

    public static void registerEvents(MultiverseCommandDestination plugin) {
        tryRegister("org.mvplugins.multiverse.core.api.event.MVTeleportDestinationEvent", new TeleportListener(plugin));
        tryRegister("org.mvplugins.multiverse.portals.event.MVPortalEvent", new PortalListener(plugin));
        tryRegister("org.mvplugins.multiverse.core.api.event.MVConfigReloadEvent", new ReloadListener(plugin));
        tryRegister("org.mvplugins.multiverse.core.api.event.MVDumpsDebugInfoEvent", new VersionListener(plugin));
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
        public void onTeleport(MVTeleportDestinationEvent event) {
            DestinationInstance<?, ?> dest = event.getDestination();
            if (!(dest instanceof CommandDestinationInstance cmdDest)) {
                return;
            }
            this.plugin.runCommand(event.getTeleporter(), event.getTeleportee(), cmdDest.getCommandKey());
            event.setCancelled(true);
        }
    }

    private static class PortalListener extends AbstractListener {
        private PortalListener(MultiverseCommandDestination plugin) {
            super(plugin);
        }
        @EventHandler
        public void onPortal(MVPortalEvent event) {
            DestinationInstance<?, ?> dest = event.getDestination();
            if (!(dest instanceof CommandDestinationInstance cmdDest)) {
                return;
            }
            this.plugin.runCommand(event.getTeleportee(), event.getTeleportee(), cmdDest.getCommandKey());
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
        public void onVersion(MVDumpsDebugInfoEvent event) {
            event.putDetailedDebugInfo("Multiverse-CommandDestination/config.yml", this.plugin.getConfigFile());
            event.appendDebugInfo(
                    "[Multiverse-CommandDestination] Multiverse-CommandDestination version: " + this.plugin.getDescription().getVersion() + "\n" +
                            "[Multiverse-CommandDestination] Loaded commands: " + this.plugin.getCommandMap() + "\n" +
                            "[Multiverse-CommandDestination] Papi installed: " + this.plugin.getCommandProvider().isPapiInstalled() + "\n" +
                            "[Multiverse-CommandDestination] Allow papi hook: " + this.plugin.getCommandProvider().isUsePapi() + "\n"
            );
        }
    }
}
