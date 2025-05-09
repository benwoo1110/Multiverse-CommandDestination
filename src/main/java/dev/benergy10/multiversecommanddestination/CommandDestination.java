package dev.benergy10.multiversecommanddestination;

import org.bukkit.command.CommandSender;
import org.mvplugins.multiverse.core.destination.Destination;
import org.mvplugins.multiverse.core.destination.DestinationSuggestionPacket;
import org.mvplugins.multiverse.core.locale.MVCorei18n;
import org.mvplugins.multiverse.core.locale.message.Message;
import org.mvplugins.multiverse.core.utils.result.Attempt;
import org.mvplugins.multiverse.core.utils.result.FailureReason;
import org.mvplugins.multiverse.external.acf.locales.MessageKey;
import org.mvplugins.multiverse.external.acf.locales.MessageKeyProvider;
import org.mvplugins.multiverse.external.jetbrains.annotations.NotNull;
import org.mvplugins.multiverse.external.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CommandDestination implements Destination<CommandDestination, CommandDestinationInstance, CommandDestination.InstanceFailureReason> {

    private final MultiverseCommandDestination plugin;

    public CommandDestination(MultiverseCommandDestination plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cmd";
    }

    @Override
    public @NotNull Attempt<CommandDestinationInstance, InstanceFailureReason> getDestinationInstance(@Nullable String commandKey) {
        if (this.plugin.getCommandMap().containsKey(commandKey)) {
            return Attempt.success(new CommandDestinationInstance(this, commandKey));
        }
        plugin.getLogger().warning("Unknown command destination key: " + commandKey);
        return Attempt.failure(InstanceFailureReason.COMMAND_NOT_FOUND, Message.of("Unknown command destination key: " + commandKey));
    }

    @Override
    public @NotNull Collection<DestinationSuggestionPacket> suggestDestinations(@NotNull CommandSender commandSender, @Nullable String s) {
        return this.plugin.getCommandMap().keySet().stream()
                .map(cmdName -> new DestinationSuggestionPacket(this, cmdName, cmdName))
                .toList();
    }

    public enum InstanceFailureReason implements FailureReason {

        COMMAND_NOT_FOUND(MVCorei18n.GENERIC_FAILURE)
        ;

        private final MessageKeyProvider messageKey;

        InstanceFailureReason(MessageKeyProvider message) {
            this.messageKey = message;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MessageKey getMessageKey() {
            return messageKey.getMessageKey();
        }
    }
}
