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
import network.sloud.hytale.portals.domain.dto.PortalCreateDto;
import network.sloud.hytale.portals.domain.entities.Network;
import network.sloud.hytale.portals.managers.PortalCreationManager;
import network.sloud.hytale.portals.stores.NetworkStore;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PortalCreatePage {

    private final NetworkStore networkStore;
    private final PortalCreationManager portalCreationManager;

    @Inject
    public PortalCreatePage(
            NetworkStore networkStore,
            PortalCreationManager portalCreationManager
    ) {
        this.networkStore = networkStore;
        this.portalCreationManager = portalCreationManager;
    }

    public void open(
            @NonNull PlayerRef playerReference,
            @NonNull Store<EntityStore> store,
            @NonNull String portalName
    ) {
        List<Network> networks = this.networkStore.getNetworks();

        if (networks.isEmpty()) {
            playerReference.sendMessage(Message.raw("No portal networks found. Please create a network first.").color(Color.RED));
            return;
        }

        TemplateProcessor template = new TemplateProcessor()
                .setVariable("networks", networks);

        PageBuilder.pageForPlayer(playerReference)
                .withLifetime(CustomPageLifetime.CanDismiss)
                .loadHtml("Pages/PortalCreatePage.html", template)
                .addEventListener("cancelButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    context.getPage().ifPresent(HyUIPage::close);
                })
                .addEventListener("nextButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    Optional<String> selectedNetworkId = context.getValue("networkSelect", String.class);

                    if (selectedNetworkId.isEmpty()) {
                        playerReference.sendMessage(Message.raw("Please select a network.").color(Color.RED));
                        return;
                    }

                    UUID networkId = UUID.fromString(selectedNetworkId.get());
                    Network network = this.networkStore.getNetworkById(networkId);

                    if (network == null) {
                        playerReference.sendMessage(Message.raw("Network not found. Please choose a valid network.").color(Color.RED));
                        return;
                    }

                    PortalCreateDto portalCreateDto = new PortalCreateDto();
                    portalCreateDto.name = portalName;
                    portalCreateDto.networkId = network.getId();
                    portalCreateDto.createdBy = playerReference.getUuid();

                    this.portalCreationManager.setPortalCreateDto(playerReference.getUuid(), portalCreateDto);

                    playerReference.sendMessage(Message.raw("Portal creation started for \"" + portalName + "\" in network \"" + network.getName() + "\".").color(Color.GREEN));
                    playerReference.sendMessage(Message.raw("Now touch two blocks to define the portal's physical area.").color(Color.GREEN));
                    playerReference.sendMessage(Message.raw("You can also type `/portal cancel` to cancel the creation of the portal.").color(Color.GREEN));

                    context.getPage().ifPresent(HyUIPage::close);
                })
                .open(store);
    }
}
