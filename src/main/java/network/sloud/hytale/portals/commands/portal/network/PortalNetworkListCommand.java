package network.sloud.hytale.portals.commands.portal.network;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import network.sloud.hytale.portals.domain.entities.Network;
import network.sloud.hytale.portals.permissions.Permissions;
import network.sloud.hytale.portals.stores.NetworkStore;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;

@Singleton
public class PortalNetworkListCommand extends CommandBase {

    private final NetworkStore networkStore;

    @Inject
    public PortalNetworkListCommand(NetworkStore networkStore) {
        super("list", "List all networks of portals.");

        this.networkStore = networkStore;

        this.addAliases("l");
        this.requirePermission(Permissions.NETWORK_LIST);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();

        if (!(sender instanceof Player)) {
            return;
        }

        List<Network> networks = this.networkStore.getNetworks();

        sender.sendMessage(Message.raw("Portal Networks:").color(Color.GREEN));

        if (networks.isEmpty()) {
            sender.sendMessage(Message.raw("No networks found.").color(Color.YELLOW));
            return;
        }

        networks.forEach(network -> sender.sendMessage(Message.raw("> \"" + network.getName() + "\" (ID: " + network.getId() + ")")));
    }
}
