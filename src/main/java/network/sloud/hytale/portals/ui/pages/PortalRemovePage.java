package network.sloud.hytale.portals.ui.pages;

import au.ellie.hyui.builders.HyUIPage;
import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.html.TemplateProcessor;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import network.sloud.hytale.portals.domain.entities.Network;
import network.sloud.hytale.portals.domain.entities.Portal;
import network.sloud.hytale.portals.domain.entities.PortalDestination;
import network.sloud.hytale.portals.stores.NetworkStore;
import network.sloud.hytale.portals.stores.PortalStore;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PortalRemovePage {

    private final NetworkStore networkStore;
    private final PortalStore portalStore;

    @Inject
    public PortalRemovePage(NetworkStore networkStore, PortalStore portalStore) {
        this.networkStore = networkStore;
        this.portalStore = portalStore;
    }

    public void open(
            @NonNull PlayerRef playerReference,
            @NonNull Store<EntityStore> store,
            @NonNull List<Portal> portals
    ) {
        var portalsData = portals.stream().map(portal -> {
            Network network = this.networkStore.getNetworkById(portal.getNetworkId());
            String networkName = network != null ? network.getName() : "Unknown Network";
            PortalDestination destination = portal.getDestination();
            String finalNetworkName = networkName;

            return new Object() {
                public final String id = portal.getId().toString();
                public final String name = portal.getName();
                public final String networkName = finalNetworkName;
                public final String destinationX = String.format("%.1f", destination.getX());
                public final String destinationY = String.format("%.1f", destination.getY());
                public final String destinationZ = String.format("%.1f", destination.getZ());
            };
        }).toList();

        TemplateProcessor template = new TemplateProcessor()
                .setVariable("portals", portalsData);

        PageBuilder.pageForPlayer(playerReference)
                .withLifetime(CustomPageLifetime.CanDismiss)
                .loadHtml("Pages/PortalRemovePage.html", template)
                .addEventListener("cancelButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    context.getPage().ifPresent(HyUIPage::close);
                })
                .addEventListener("removeButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    Optional<String> selectedPortalIdString = context.getValue("portalSelect", String.class);

                    if (selectedPortalIdString.isEmpty()) {
                        playerReference.sendMessage(Message.raw("Please select a portal.").color(Color.RED));
                        return;
                    }

                    UUID portalId = UUID.fromString(selectedPortalIdString.get());
                    Optional<Portal> portal = this.portalStore.getPortalById(portalId);

                    if (portal.isEmpty()) {
                        playerReference.sendMessage(Message.raw("Portal not found.").color(Color.RED));
                        return;
                    }

                    this.portalStore.remove(portal.get().getId());
                    playerReference.sendMessage(Message.raw("Portal '" + portal.get().getName() + "' removed successfully.").color(Color.GREEN));

                    context.getPage().ifPresent(HyUIPage::close);
                })
                .open(store);
    }
}
