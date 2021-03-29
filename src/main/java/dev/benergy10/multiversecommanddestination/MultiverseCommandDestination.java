package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiversePortals.MultiversePortals;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MultiverseCommandDestination extends JavaPlugin {

    private boolean doPapiHook = true;
    private final Map<String, List<String>> commandMap = new HashMap<>();

    private MultiverseCore core;
    private MultiversePortals portals;
    private PlaceholderAPIPlugin papi;

    @Override
    public void onEnable() {
        this.core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        this.portals = (MultiversePortals) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Portals");
        this.papi = (PlaceholderAPIPlugin) Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI");

        this.core.getDestFactory().registerDestinationType(CommandDestination.class, "cmd");

        CoreListener.registerEvents(this);
        Bukkit.getPluginManager().registerEvents(new TeleportListener(this), this);
        if (this.portals != null) {
            Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
        }

        this.loadConfig();
    }

    public void loadConfig() {
        this.commandMap.clear();
        this.saveDefaultConfig();
        this.reloadConfig();
        FileConfiguration config = this.getConfig();
        ConfigurationSection commandSection = config.getConfigurationSection("commands");
        if (commandSection == null) {
            return;
        }
        for (String cmdName : commandSection.getKeys(false)) {
            List<String> commandList = commandSection.getStringList(cmdName);
            this.commandMap.put(cmdName.toLowerCase(), commandList);
        }
        this.doPapiHook = config.getBoolean("enable-papi-hook", true);
    }

    public void runCommand(Entity entity, String cmdName) {
        List<String> commandList =this.commandMap.get(cmdName.toLowerCase());
        if (commandList == null) {
            entity.sendMessage("No such command: " + cmdName);
            return;
        }
        for (String command : commandList) {
            CommandSender targetExecutor = entity;
            if (command.startsWith("console:")) {
                targetExecutor = Bukkit.getConsoleSender();
                command = command.substring(8);
            }

            command = command.replaceAll("%player%", entity.getName())
                    .replaceAll("%world%", entity.getWorld().getName());

            if (this.papi != null && this.doPapiHook && entity instanceof Player) {
                command = PlaceholderAPI.setPlaceholders((Player) entity, command);
            }

            Bukkit.dispatchCommand(targetExecutor, command);
        }
    }

    public @NotNull @Unmodifiable Map<String, List<String>> getCommandMap() {
        return commandMap;
    }

    public boolean isDoPapiHook() {
        return doPapiHook;
    }

    public @Nullable MultiverseCore getCore() {
        return core;
    }

    public @Nullable MultiversePortals getPortals() {
        return portals;
    }

    public @Nullable PlaceholderAPIPlugin getPapi() {
        return papi;
    }
}
