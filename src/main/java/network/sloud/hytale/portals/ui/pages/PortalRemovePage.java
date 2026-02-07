package network.sloud.hytale.portals.ui.pages;

import au.ellie.hyui.builders.HyUIPage;
import au.ellie.hyui.builders.PageBuilder;
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
        String portalsOptions = this.buildPortalsOptions(portals);

        String html = """
                <style>
                  .container {
                    anchor-width: 500;
                    anchor-height: 250;
                  }
                  .container-contents {
                  }
                  .section {
                    layout-mode: top;
                    anchor-height: 90;
                  }
                  .section-header {
                    font-size: 14;
                    font-weight: bold;
                    color: #AAAAAA;
                    text-transform: uppercase;
                    anchor-height: 20;
                  }
                  .description {
                    font-size: 14;
                    color: #888888;
                    anchor-height: 25;
                  }
                  .content-spacer {
                    flex-weight: 1;
                  }
                  .button-row {
                    layout-mode: right;
                    anchor-height: 50;
                  }
                  .button-spacer {
                    anchor-width: 20;
                  }
                </style>
                
                <div class="page-overlay">
                    <div class="container" data-hyui-title="Remove Portal">
                        <div class="container-contents">
                
                            <div class="section">
                              <p class="section-header">Select Portal</p>
                              <p class="description">Multiple portals found with this name. Please select which one to remove.</p>
                              <select id="portalSelect">
                                %s
                              </select>
                            </div>
                
                            <div class="content-spacer"></div>
                
                            <div class="button-row">
                              <button id="removeButton">Remove</button>
                              <div class="button-spacer"></div>
                              <input type="reset" id="cancelButton" value="Cancel"/>
                            </div>
                        </div>
                    </div>
                </div>
                """.formatted(portalsOptions);

        PageBuilder.pageForPlayer(playerReference)
                .withLifetime(CustomPageLifetime.CanDismiss)
                .fromHtml(html)
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

    private String buildPortalsOptions(List<Portal> portals) {
        StringBuilder options = new StringBuilder();

        for (Portal portal : portals) {
            Network network = this.networkStore.getNetworkById(portal.getNetworkId());
            String networkName = network != null ? network.getName() : "Unknown Network";

            PortalDestination destination = portal.getDestination();
            String destinationInfo = String.format("[%.1f, %.1f, %.1f]", destination.getX(), destination.getY(), destination.getZ());

            options.append(String.format(
                    "<option value=\"%s\">%s (Network \"%s\") %s</option>",
                    portal.getId(),
                    portal.getName(),
                    networkName,
                    destinationInfo
            ));
        }

        return options.toString();
    }
}
