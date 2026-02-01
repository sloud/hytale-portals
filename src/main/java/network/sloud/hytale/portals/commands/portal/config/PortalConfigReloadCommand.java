package network.sloud.hytale.portals.commands.portal.config;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import network.sloud.hytale.portals.config.PortalsConfig;
import network.sloud.hytale.portals.permissions.Permissions;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class PortalConfigReloadCommand extends CommandBase {

    private final PortalsConfig portalsConfig;

    @Inject
    public PortalConfigReloadCommand(PortalsConfig portalsConfig) {
        super("reload", "Starts creation process of a new portal.");

        this.portalsConfig = portalsConfig;

        this.requirePermission(Permissions.ADMIN);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();

        if (!(sender instanceof Player)) {
            // Logger will output the reload message
            this.portalsConfig.reload();
            return;
        }

        this.portalsConfig.reload();

        sender.sendMessage(Message.raw("Reloaded portals configuration.").color(Color.GREEN));
    }
}
