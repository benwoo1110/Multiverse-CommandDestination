package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiversePortals.MultiversePortals;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MultiverseCommandDestination extends JavaPlugin {

    private final Map<String, List<String>> commandMap = new HashMap<>();

    private MultiverseCore core;
    private MultiversePortals portals;

    @Override
    public void onEnable() {
        this.core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        this.portals = (MultiversePortals) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Portals");

        this.core.getDestFactory().registerDestinationType(CommandDestination.class, "cmd");

        Bukkit.getPluginManager().registerEvents(new CoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TeleportListener(this), this);
        if (this.portals != null) {
            Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
        }

        this.loadConfig();
    }

    @Override
    public void onDisable() {
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
            Bukkit.dispatchCommand(targetExecutor, command);
        }
    }
}
