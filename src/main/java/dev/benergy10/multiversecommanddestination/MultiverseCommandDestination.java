package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MultiverseCommandDestination extends JavaPlugin {

    private static final String CONFIG_FILENAME = "config.yml";

    private boolean portalsInstalled;
    private boolean papiInstalled;

    private final Map<String, List<String>> commandMap = new HashMap<>();
    private boolean doPapiHook = true;

    @Override
    public void onEnable() {
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (core == null) {
            this.getLogger().info("Multiverse-Core is not installed on your server.");
            this.getLogger().info("CommandDestination will not work!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        core.getDestFactory().registerDestinationType(CommandDestination.class, "cmd");
        MultiverseListeners.registerEvents(this);

        this.portalsInstalled = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Portals") != null;
        this.papiInstalled = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        this.loadConfig();
    }

    public void loadConfig() {
        this.commandMap.clear();
        File configFile = this.getConfigFile();
        if (!configFile.exists()) {
            this.saveResource(CONFIG_FILENAME, false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
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
        List<String> commandList = this.commandMap.get(cmdName.toLowerCase());
        if (commandList == null) {
            entity.sendMessage("No such command destination with name: " + cmdName);
            if (this.isDefaultCommand()) {
                entity.sendMessage("It looks like you have not setup any command destinations. Please do so in CommandDestination config.yml file.");
            }
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

            if (this.papiInstalled && this.doPapiHook && entity instanceof Player) {
                command = PlaceholderAPI.setPlaceholders((Player) entity, command);
            }

            Bukkit.dispatchCommand(targetExecutor, command);
        }
    }

    private boolean isDefaultCommand() {
        return this.commandMap.isEmpty() || (this.commandMap.size() == 1 && this.commandMap.containsKey("examplename"));
    }

    public @NotNull File getConfigFile() {
        return new File(this.getDataFolder(), CONFIG_FILENAME);
    }

    public @NotNull @Unmodifiable Map<String, List<String>> getCommandMap() {
        return commandMap;
    }

    public boolean isDoPapiHook() {
        return doPapiHook;
    }

    public boolean isPortalsInstalled() {
        return portalsInstalled;
    }

    public boolean isPapiInstalled() {
        return papiInstalled;
    }
}
