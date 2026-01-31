package dev.joeyaurel.hytale.portals.commands.portal;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import dev.joeyaurel.hytale.portals.commands.portal.config.PortalConfigReloadCommand;
import dev.joeyaurel.hytale.portals.permissions.Permissions;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PortalConfigCommand extends CommandBase {

    @Inject
    public PortalConfigCommand(PortalConfigReloadCommand portalConfigReloadCommand) {
        super("config", "Manages configuration of the plugin.");

        this.requirePermission(Permissions.ADMIN);

        this.addSubCommand(portalConfigReloadCommand);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        Message usageMessage = this.getUsageString(commandContext.sender());
        commandContext.sender().sendMessage(usageMessage);
    }
}
