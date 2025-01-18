package dev.benergy10.multiversecommanddestination;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.mvplugins.multiverse.core.api.destination.DestinationInstance;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;
import org.mvplugins.multiverse.external.vavr.control.Option;

public class CommandDestinationInstance extends DestinationInstance<CommandDestinationInstance, CommandDestination> {

    private final String commandKey;

    protected CommandDestinationInstance(@NotNull CommandDestination destination, @NotNull String commandKey) {
        super(destination);
        this.commandKey = commandKey;
    }

    public String getCommandKey() {
        return commandKey;
    }

    @Override
    public @NotNull Option<Location> getLocation(@NotNull Entity entity) {
        return Option.of(entity.getLocation());
    }

    @Override
    public @NotNull Option<Vector> getVelocity(@NotNull Entity entity) {
        return Option.none();
    }

    @Override
    public boolean checkTeleportSafety() {
        return false;
    }

    @Override
    public @NotNull Option<String> getFinerPermissionSuffix() {
        return Option.none();
    }

    @Override
    protected @NotNull String serialise() {
        return commandKey;
    }
}
