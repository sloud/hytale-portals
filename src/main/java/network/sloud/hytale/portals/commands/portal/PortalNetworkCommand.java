package network.sloud.hytale.portals.commands.portal;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import network.sloud.hytale.portals.commands.portal.network.PortalNetworkCreateCommand;
import network.sloud.hytale.portals.commands.portal.network.PortalNetworkEditCommand;
import network.sloud.hytale.portals.commands.portal.network.PortalNetworkListCommand;
import network.sloud.hytale.portals.commands.portal.network.PortalNetworkRemoveCommand;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PortalNetworkCommand extends CommandBase {

    @Inject
    public PortalNetworkCommand(
            PortalNetworkCreateCommand portalNetworkCreateCommand,
            PortalNetworkEditCommand portalNetworkEditCommand,
            PortalNetworkListCommand portalNetworkListCommand,
            PortalNetworkRemoveCommand portalNetworkRemoveCommand
    ) {
        super("network", "Manages networks of portals.");

        this.addAliases("n", "networks");

        this.addSubCommand(portalNetworkCreateCommand);
        this.addSubCommand(portalNetworkEditCommand);
        this.addSubCommand(portalNetworkListCommand);
        this.addSubCommand(portalNetworkRemoveCommand);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        Message usageMessage = this.getUsageString(commandContext.sender());
        commandContext.sender().sendMessage(usageMessage);
    }
}
