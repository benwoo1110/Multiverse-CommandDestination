package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import dev.benergy10.commandexecutorapi.CommandGroup;
import dev.benergy10.commandexecutorapi.CommandProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MultiverseCommandDestination extends JavaPlugin {

    private static final String CONFIG_FILENAME = "config.yml";

    private CommandProvider commandProvider;
    private final Map<String, CommandGroup> commandMap = new HashMap<>();

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

        this.commandProvider = new CommandProvider();

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
            this.commandMap.put(cmdName.toLowerCase(), this.commandProvider.toCommandGroup(commandList));
        }
        commandProvider.setUsePapi(config.getBoolean("enable-papi-hook", true));
    }

    public void runCommand(CommandSender sender, Entity entity, String cmdName) {
        CommandGroup commandGroup = this.commandMap.get(cmdName.toLowerCase());
        if (commandGroup == null) {
            sender.sendMessage("No such command destination with key: " + ChatColor.RED + cmdName);
            return;
        }
        commandGroup.executeAll(entity);
    }

    public @NotNull File getConfigFile() {
        return new File(this.getDataFolder(), CONFIG_FILENAME);
    }

    public @NotNull CommandProvider getCommandProvider() {
        return commandProvider;
    }

    public @NotNull @Unmodifiable Map<String, CommandGroup> getCommandMap() {
        return commandMap;
    }
}
