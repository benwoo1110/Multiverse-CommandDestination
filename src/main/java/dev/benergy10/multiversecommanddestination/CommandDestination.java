package dev.benergy10.multiversecommanddestination;

import com.onarandombox.MultiverseCore.api.MVDestination;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class CommandDestination implements MVDestination {

    private boolean isValid;
    private String targetCommand;

    @Override
    public String getIdentifier() {
        return "cmd";
    }

    @Override
    public boolean isThisType(JavaPlugin plugin, String destination) {
        String[] items = destination.split(":");
        if (items.length != 2) {
            return false;
        }
        return items[0].equalsIgnoreCase("cmd");
    }

    @Override
    public Location getLocation(Entity entity) {
        return entity.getLocation();
    }

    @Override
    public Vector getVelocity() {
        return new Vector(0, 0, 0);
    }

    @Override
    public void setDestination(JavaPlugin plugin, String destination) {
        String[] items = destination.split(":");
        if (items.length != 2) {
            this.isValid = false;
            return;
        }
        this.targetCommand = items[1];
        this.isValid = true;
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public String getType() {
        return "Command";
    }

    @Override
    public String getName() {
        return this.targetCommand;
    }

    @Override
    public String toString() {
        return "cmd:" + this.targetCommand;
    }

    @Override
    public String getRequiredPermission() {
        return "";
    }

    @Override
    public boolean useSafeTeleporter() {
        return false;
    }
}
