package network.sloud.hytale.portals.ui.pages;

import au.ellie.hyui.builders.HyUIPage;
import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.html.TemplateProcessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import network.sloud.hytale.portals.domain.entities.Portal;
import network.sloud.hytale.portals.managers.PortalTeleportationManager;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Singleton
public class PortalTeleportPage {

    private final HytaleLogger logger;
    private final PortalTeleportationManager portalTeleportationManager;

    @Inject
    public PortalTeleportPage(
            HytaleLogger logger,
            PortalTeleportationManager portalTeleportationManager
    ) {
        this.logger = logger;
        this.portalTeleportationManager = portalTeleportationManager;
    }

    public void open(
            @NonNull PlayerRef playerReference,
            @NonNull Ref<EntityStore> playerEntityRef,
            @NonNull Store<EntityStore> store,
            @NonNull List<Portal> availablePortals,
            @NonNull Consumer<UUID> onDismiss
    ) {
        TemplateProcessor template = new TemplateProcessor()
                .setVariable("portals", availablePortals);

        PageBuilder.pageForPlayer(playerReference)
                .withLifetime(CustomPageLifetime.CantClose)
                .loadHtml("Pages/PortalTeleportPage.html", template)
                .addEventListener("cancelButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    context.getPage().ifPresent(HyUIPage::close);
                    onDismiss.accept(playerReference.getUuid());
                })
                .addEventListener("teleportButton", CustomUIEventBindingType.Activating, (ignored, context) -> {
                    Optional<String> selectedPortalId = context.getValue("portalSelect", String.class);

                    if (selectedPortalId.isEmpty()) {
                        playerReference.sendMessage(Message.raw("Please select a destination portal.").color(Color.RED));
                        return;
                    }

                    UUID portalId = UUID.fromString(selectedPortalId.get());

                    Portal targetPortal = availablePortals.stream()
                            .filter(p -> p.getId().equals(portalId))
                            .findFirst()
                            .orElse(null);

                    if (targetPortal == null) {
                        playerReference.sendMessage(Message.raw("Selected portal not found.").color(Color.RED));

                        context.getPage().ifPresent(HyUIPage::close);

                        onDismiss.accept(playerReference.getUuid());

                        return;
                    }

                    this.portalTeleportationManager.teleportPlayerToPortal(
                            playerReference.getUuid(),
                            portalId,
                            onDismiss,
                            null
                    );

                    context.getPage().ifPresent(HyUIPage::close);
                    onDismiss.accept(playerReference.getUuid());
                })
                .open(store);
    }
}
