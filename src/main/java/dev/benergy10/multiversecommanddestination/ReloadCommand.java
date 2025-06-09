package dev.benergy10.multiversecommanddestination;

import org.mvplugins.multiverse.external.acf.commands.BaseCommand;
import org.mvplugins.multiverse.external.acf.commands.BukkitCommandIssuer;
import org.mvplugins.multiverse.external.acf.commands.annotation.CommandAlias;
import org.mvplugins.multiverse.external.acf.commands.annotation.Subcommand;

@CommandAlias("mvcd")
public class ReloadCommand extends BaseCommand {

    private final MultiverseCommandDestination plugin;

    ReloadCommand(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    public void reload(BukkitCommandIssuer issuer) {
        plugin.reloadConfig();
        issuer.sendMessage("Successfully reloaded command destinations!");
    }
}
