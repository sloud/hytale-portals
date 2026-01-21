package dev.joeyaurel.hytale.portals.entities;

import java.util.UUID;

public class PortalBound {
    private UUID id;
    private UUID portalId;
    private int locationX;
    private int locationY;
    private int locationZ;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        if (this.id != null) {
            throw new IllegalStateException("ID is already set and cannot be changed.");
        }

        this.id = id;
    }

    public UUID getPortalId() {
        return portalId;
    }

    public void setPortalId(UUID portalId) {
        this.portalId = portalId;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }
}
