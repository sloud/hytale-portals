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
import network.sloud.hytale.portals.domain.dto.NetworkUpdateDto;
import network.sloud.hytale.portals.domain.entities.Network;
import network.sloud.hytale.portals.domain.entities.Portal;
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
public class PortalNetworkEditPage {

    private final NetworkStore networkStore;
    private final PortalStore portalStore;

    @Inject
    public PortalNetworkEditPage(
            NetworkStore networkStore,
            PortalStore portalStore
    ) {
        this.networkStore = networkStore;
        this.portalStore = portalStore;
    }

    public void open(
            @NonNull PlayerRef playerReference,
            @NonNull Store<EntityStore> store,
            @NonNull UUID networkId
    ) {
        Network network = this.networkStore.getNetworkById(networkId);

        if (network == null) {
            return;
        }

        List<Portal> portals = this.portalStore.getPortalsInNetwork(network.getId());

        TemplateProcessor template = new TemplateProcessor()
                .setVariable("networkName", network.getName())
                .setVariable("hasPortals", !portals.isEmpty())
                .setVariable("portals", portals);

        PageBuilder.pageForPlayer(playerReference)
                .withLifetime(CustomPageLifetime.CanDismiss)
                .loadHtml("Pages/PortalNetworkEditPage.html", template)
                .addEventListener("closeButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    context.getPage().ifPresent(HyUIPage::close);
                })
                .addEventListener("saveButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    Optional<String> newNetworkName = context.getValue("nameInput", String.class);

                    NetworkUpdateDto networkUpdateDto = new NetworkUpdateDto();
                    networkUpdateDto.id = network.getId();
                    networkUpdateDto.name = newNetworkName.orElse(network.getName());

                    Network newNetwork = this.networkStore.updateNetwork(networkUpdateDto);

                    playerReference.sendMessage(Message.raw("Portal network \"" + network.getName() + "\" has been updated to \"" + newNetwork.getName() + "\".").color(Color.GREEN));

                    context.getPage().ifPresent(HyUIPage::close);
                })
                .open(store);
    }
}
