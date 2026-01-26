package dev.joeyaurel.hytale.portals.systems.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.joeyaurel.hytale.portals.domain.dto.PortalCreateDto;
import dev.joeyaurel.hytale.portals.domain.managers.PortalCreationManager;
import dev.joeyaurel.hytale.portals.geometry.Vector;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.*;

@Singleton
public class BreakBlockEventSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {

    private final PortalCreationManager portalCreationManager;

    private final Query<EntityStore> query;

    @Inject
    public BreakBlockEventSystem(PortalCreationManager portalCreationManager) {
        super(BreakBlockEvent.class);

        this.portalCreationManager = portalCreationManager;

        this.query = Query.and(Player.getComponentType());
    }

    @Override
    public void handle(
            final int index,
            @Nonnull final ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull final Store<EntityStore> store,
            @Nonnull final CommandBuffer<EntityStore> commandBuffer,
            @Nonnull final BreakBlockEvent event
    ) {
        Ref<EntityStore> reference = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(reference, Player.getComponentType());
        PlayerRef playerReference = store.getComponent(reference, PlayerRef.getComponentType());

        if (player == null || playerReference == null) {
            return;
        }

        if (player.getGameMode() != GameMode.Creative) {
            return;
        }

        UUID playerId = playerReference.getUuid();

        if (!this.portalCreationManager.isPlayerCreatingPortal(playerId)) {
            return;
        }

        World playerWorld = player.getWorld();

        if (playerWorld == null) {
            return;
        }

        PortalCreateDto portalCreateDto = this.portalCreationManager.getPortalCreateDto(playerId);

        if (portalCreateDto == null) {
            return;
        }

        // Cancel damaging the block if in portal creation mode
        event.setCancelled(true);

        UUID worldId = playerWorld.getWorldConfig().getUuid();

        if (portalCreateDto.worldId == null) {
            portalCreateDto.worldId = worldId;
        } else if (portalCreateDto.worldId != worldId) {
            playerReference.sendMessage(Message.raw("Portal creation world changed. Resetting bounds.").color(Color.YELLOW));

            portalCreateDto.worldId = worldId;
            portalCreateDto.bounds = new ArrayList<>();
        }

        if (portalCreateDto.bounds == null) {
            portalCreateDto.bounds = new ArrayList<>();
        } if (portalCreateDto.bounds.size() + 1 > 2) {
            playerReference.sendMessage(Message.raw("Portal creation bounds limit of 2 reached. Resetting bounds.").color(Color.YELLOW));

            portalCreateDto.bounds = new ArrayList<>();
        }

        Vector3i targetBlock = event.getTargetBlock();

        int x = targetBlock.getX();
        int y = targetBlock.getY();
        int z = targetBlock.getZ();

        Vector portalBound = new Vector(x, y, z);
        portalCreateDto.bounds.add(portalBound);

        this.portalCreationManager.setPortalCreateDto(playerId, portalCreateDto);

        if (portalCreateDto.bounds.size() == 1) {
            playerReference.sendMessage(Message.raw("[A] Portal bound A set. (X: " + x + ", Y: " + y + ", Z: " + z + ") Touch another block to set bound B.").color(Color.GREEN));
            playerReference.sendMessage(Message.raw("Or cancel portal creation with `/portal cancel`.").color(Color.GRAY));
        } else if (portalCreateDto.bounds.size() == 2) {
            playerReference.sendMessage(Message.raw("[B] Portal bound B set. (X: " + x + ", Y: " + y + ", Z: " + z + ") Finish portal creation with `/portal done` at the destination, facing in the correct direction.").color(Color.GREEN));
            playerReference.sendMessage(Message.raw("Or cancel portal creation with `/portal cancel`.").color(Color.GRAY));
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return this.query;
    }

    @NonNullDecl
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return Collections.singleton(RootDependency.first());
    }
}
