package network.sloud.hytale.portals.commands.portal;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.FlagArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import network.sloud.hytale.portals.domain.entities.Portal;
import network.sloud.hytale.portals.permissions.Permissions;
import network.sloud.hytale.portals.stores.PortalStore;
import network.sloud.hytale.portals.ui.pages.PortalRemovePage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class PortalRemoveCommand extends AbstractAsyncCommand {

    private final PortalStore portalStore;
    private final PortalRemovePage portalRemovePage;

    private final RequiredArg<String> portalName;
    private final FlagArg confirm;

    @Inject
    public PortalRemoveCommand(PortalStore portalStore, PortalRemovePage portalRemovePage) {
        super("remove", "Removes an existing portal.");

        this.portalStore = portalStore;
        this.portalRemovePage = portalRemovePage;

        this.portalName = this.withRequiredArg("name", "Name of the portal.", ArgTypes.STRING);
        this.confirm = this.withFlagArg("confirm", "Confirm the removal of the network.");

        this.addAliases("rm", "r", "delete", "del");
        this.requirePermission(Permissions.PORTAL_DELETE);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext commandContext) {
        CommandSender sender = commandContext.sender();

        if (!(sender instanceof Player player)) {
            return CompletableFuture.completedFuture(null);
        }

        String portalName = this.portalName.get(commandContext);

        // Remove leading and trailing quotes
        portalName = portalName.replaceAll("^\"|\"$", "");

        List<Portal> portals = this.portalStore.getPortalsByName(portalName);

        if (portals.isEmpty()) {
            sender.sendMessage(Message.raw("No portal found with name '" + portalName + "'.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        boolean isConfirmed = this.confirm.get(commandContext);

        if (!isConfirmed) {
            sender.sendMessage(Message.raw("Are you sure you want to remove this portal? Type `/portal remove \"" + portalName + "\" --confirm` to confirm.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        if (portals.size() == 1) {
            Portal portal = portals.getFirst();

            this.portalStore.remove(portal.getId());

            sender.sendMessage(Message.raw("Portal '" + portal.getName() + "' removed successfully.").color(Color.GREEN));

            return CompletableFuture.completedFuture(null);
        }

        Ref<EntityStore> reference = player.getReference();

        if (reference == null || !reference.isValid()) {
            return CompletableFuture.completedFuture(null);
        }

        Store<EntityStore> store = reference.getStore();
        World world = store.getExternalData().getWorld();

        return CompletableFuture.runAsync(() -> {
            PlayerRef playerReference = store.getComponent(reference, PlayerRef.getComponentType());

            if (playerReference == null) {
                return;
            }

            this.portalRemovePage.open(playerReference, store, portals);
        }, world);
    }
}
