package dev.joeyaurel.hytale.portals.managers;

import com.hypixel.hytale.math.vector.Transform;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PortalEnterManager {
    private final Map<UUID, List<Transform>> playersLastPositions;

    public PortalEnterManager() {
        playersLastPositions =  new ConcurrentHashMap<>();
    }
}
