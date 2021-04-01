package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.MultiverseCore;
import dev.benergy10.commandexecutorapi.CommandGroup;
import dev.benergy10.commandexecutorapi.CommandProvider;
import org.bukkit.Bukkit;
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

    private boolean portalsInstalled;
    private boolean papiInstalled;

    private CommandProvider commandProvider;

    private final Map<String, CommandGroup> commandMap = new HashMap<>();
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
        this.doPapiHook = config.getBoolean("enable-papi-hook", true);
    }

    public void runCommand(CommandSender sender, Entity entity, String cmdName) {
        CommandGroup commandGroup = this.commandMap.get(cmdName.toLowerCase());
        if (commandGroup == null) {
            sender.sendMessage("No such command destination with name: " + cmdName);
            if (this.isDefaultCommand()) {
                sender.sendMessage("It looks like you have not setup any command destinations. Please do so in CommandDestination config.yml file.");
            }
            return;
        }
        commandGroup.executeAll(entity);
    }

    private boolean isDefaultCommand() {
        return this.commandMap.isEmpty() || (this.commandMap.size() == 1 && this.commandMap.containsKey("examplename"));
    }

    public @NotNull File getConfigFile() {
        return new File(this.getDataFolder(), CONFIG_FILENAME);
    }

    public CommandProvider getCommandProvider() {
        return commandProvider;
    }

    public @NotNull @Unmodifiable Map<String, CommandGroup> getCommandMap() {
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
