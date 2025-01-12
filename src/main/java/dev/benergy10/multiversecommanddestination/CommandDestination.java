package dev.benergy10.multiversecommanddestination;

import org.mvplugins.multiverse.core.destination.Destination;
import org.mvplugins.multiverse.external.acf.commands.BukkitCommandIssuer;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;
import org.mvplugins.multiverse.external.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CommandDestination implements Destination<CommandDestination, CommandDestinationInstance> {

    private final MultiverseCommandDestination plugin;

    public CommandDestination(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cmd";
    }

    @Override
    public @Nullable CommandDestinationInstance getDestinationInstance(@Nullable String commandKey) {
        if (this.plugin.getCommandMap().containsKey(commandKey)) {
            return new CommandDestinationInstance(this, commandKey);
        }
        plugin.getLogger().warning("Unknown command destination key: " + commandKey);
        return null;
    }

    @Override
    public @NotNull Collection<String> suggestDestinations(@NotNull BukkitCommandIssuer bukkitCommandIssuer, @Nullable String s) {
        return plugin.getCommandMap().keySet();
    }
}
