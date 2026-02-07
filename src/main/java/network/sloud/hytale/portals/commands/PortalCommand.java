package network.sloud.hytale.portals.commands;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import network.sloud.hytale.portals.SloudPortalsPlugin;
import network.sloud.hytale.portals.commands.portal.*;

import javax.annotation.Nonnull;

@Singleton
public class PortalCommand extends CommandBase {

    @Inject
    public PortalCommand(
            SloudPortalsPlugin plugin,
            PortalCreateCommand portalCreateCommand,
            PortalConfigCommand portalConfigCommand,
            PortalCancelCommand portalCancelCommand,
            PortalDoneCommand portalDoneCommand,
            PortalNetworkCommand portalNetworkCommand,
            PortalRemoveCommand portalRemoveCommand
    ) {
        super("portal", "Prints basic infos for the " + plugin.getPluginName() + " plugin.");

        this.addAliases("portals");

        this.addSubCommand(portalCreateCommand);
        this.addSubCommand(portalConfigCommand);
        this.addSubCommand(portalCancelCommand);
        this.addSubCommand(portalDoneCommand);
        this.addSubCommand(portalNetworkCommand);
        this.addSubCommand(portalRemoveCommand);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        Message usageMessage = this.getUsageString(commandContext.sender());
        commandContext.sendMessage(usageMessage);
    }
}
