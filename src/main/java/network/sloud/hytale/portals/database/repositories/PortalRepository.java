package network.sloud.hytale.portals.database.repositories;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hypixel.hytale.logger.HytaleLogger;
import network.sloud.hytale.portals.database.Database;
import network.sloud.hytale.portals.domain.dto.PortalCreateDto;
import network.sloud.hytale.portals.domain.entities.Portal;
import network.sloud.hytale.portals.domain.entities.PortalBound;
import network.sloud.hytale.portals.domain.entities.PortalDestination;
import network.sloud.hytale.portals.geometry.Vector;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Singleton
public class PortalRepository {

    private final Database database;
    private final HytaleLogger logger;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    @Inject
    public PortalRepository(Database database, HytaleLogger logger) {
        this.database = database;
        this.logger = logger;
    }

    public List<Portal> listAllPortals() {
        this.logger.atFine().log("Fetching portals from database...");

        List<Portal> portals = new ArrayList<>();
        Connection connection = this.database.getConnection();
        String sql = "SELECT id, name, world_id, network_id, sort_order, created_by, created_at FROM portals";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Portal portal = mapToPortalEntity(resultSet);
                portal.setBounds(getBoundsForPortal(portal.getId()));
                portal.setDestination(getDestinationForPortal(portal.getId()));
                portals.add(portal);
            }
        } catch (SQLException e) {
            this.logger.atSevere().log("Error listing portals: " + e.getMessage());
        }

        return portals;
    }

    public Portal createPortal(PortalCreateDto portalCreateDto) {
        this.logger.atFine().log("Creating portal with data name='" + portalCreateDto.name + "', worldId='" + portalCreateDto.worldId + "', networkId='" + portalCreateDto.networkId + "', bounds='" + portalCreateDto.bounds + "', createdBy='" + portalCreateDto.createdBy + "', yaw='" + portalCreateDto.destinationBodyYaw + "', pitch='" + portalCreateDto.destinationHeadPitch + "'...");

        if (!portalCreateDto.isValid()) {
            return null;
        }

        UUID portalId = UUID.randomUUID();
        Date createdAt = new Date();

        Connection connection = this.database.getConnection();

        try {
            connection.setAutoCommit(false);

            // 1. Insert Portal
            String portalSql = "INSERT INTO portals (id, name, world_id, network_id, created_by, created_at) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(portalSql)) {
                statement.setString(1, portalId.toString());
                statement.setString(2, portalCreateDto.name);
                statement.setString(3, portalCreateDto.worldId.toString());
                statement.setString(4, portalCreateDto.networkId.toString());
                statement.setString(5, portalCreateDto.createdBy.toString());
                statement.setString(6, this.dateFormat.format(createdAt));
                statement.executeUpdate();
            }

            // 2. Insert Bounds
            String boundSql = "INSERT INTO portal_bounds (id, portal_id, x, y, z) VALUES (?, ?, ?, ?, ?)";
            List<PortalBound> bounds = new ArrayList<>();

            for (Vector boundVector : portalCreateDto.bounds) {
                UUID boundId = UUID.randomUUID();

                try (PreparedStatement statement = connection.prepareStatement(boundSql)) {
                    statement.setString(1, boundId.toString());
                    statement.setString(2, portalId.toString());
                    statement.setInt(3, (int) boundVector.x());
                    statement.setInt(4, (int) boundVector.y());
                    statement.setInt(5, (int) boundVector.z());
                    statement.executeUpdate();
                }

                PortalBound bound = new PortalBound();

                bound.setId(boundId);
                bound.setPortalId(portalId);
                bound.setX((int) boundVector.x());
                bound.setY((int) boundVector.y());
                bound.setZ((int) boundVector.z());

                bounds.add(bound);
            }

            // 3. Insert Destination
            String destSql = "INSERT INTO portal_destinations (id, portal_id, x, y, z, body_yaw, head_yaw, head_pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            UUID destId = UUID.randomUUID();

            try (PreparedStatement statement = connection.prepareStatement(destSql)) {
                statement.setString(1, destId.toString());
                statement.setString(2, portalId.toString());
                statement.setDouble(3, portalCreateDto.destinationPosition.x());
                statement.setDouble(4, portalCreateDto.destinationPosition.y());
                statement.setDouble(5, portalCreateDto.destinationPosition.z());
                statement.setFloat(6, portalCreateDto.destinationBodyYaw);
                statement.setFloat(7, portalCreateDto.destinationHeadYaw);
                statement.setFloat(8, portalCreateDto.destinationHeadPitch);
                statement.executeUpdate();
            }

            connection.commit();

            PortalDestination destination = new PortalDestination();

            destination.setId(destId);
            destination.setPortalId(portalId);
            destination.setX(portalCreateDto.destinationPosition.x());
            destination.setY(portalCreateDto.destinationPosition.y());
            destination.setZ(portalCreateDto.destinationPosition.z());
            destination.setBodyYaw(portalCreateDto.destinationBodyYaw);
            destination.setHeadYaw(portalCreateDto.destinationHeadYaw);
            destination.setHeadPitch(portalCreateDto.destinationHeadPitch);

            Portal portal = new Portal();

            portal.setId(portalId);
            portal.setName(portalCreateDto.name);
            portal.setWorldId(portalCreateDto.worldId);
            portal.setNetworkId(portalCreateDto.networkId);
            portal.setCreatedBy(portalCreateDto.createdBy);
            portal.setCreatedAt(createdAt);
            portal.setBounds(bounds);
            portal.setDestination(destination);

            return portal;

        } catch (SQLException e) {
            this.logger.atSevere().log("Error creating portal: " + e.getMessage());

            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                this.logger.atSevere().log("Error rolling back portal creation: " + rollbackEx.getMessage());
            }

            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                this.logger.atSevere().log("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public void deletePortal(UUID id) {
        this.logger.atFine().log("Deleting portal with id '" + id + "'...");

        Connection connection = this.database.getConnection();
        String sql = "DELETE FROM portals WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            this.logger.atSevere().log("Error deleting portal " + id + ": " + e.getMessage());
        }
    }

    public void updatePortal(Portal portal) {
        this.logger.atFine().log("Updating portal with id '" + portal.getId() + "'...");

        Connection connection = this.database.getConnection();
        String sql = "UPDATE portals SET name = ?, sort_order = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, portal.getName());
            statement.setInt(2, portal.getSortOrder());
            statement.setString(3, portal.getId().toString());
            statement.executeUpdate();

            // Note: In a real scenario, we might also want to update bounds and destination,
            // but for now we focus on basic portal metadata as per requirements.
        } catch (SQLException e) {
            this.logger.atSevere().log("Error updating portal " + portal.getId() + ": " + e.getMessage());
        }
    }

    private Portal mapToPortalEntity(ResultSet resultSet) throws SQLException {
        Portal portal = new Portal();
        portal.setId(UUID.fromString(resultSet.getString("id")));
        portal.setName(resultSet.getString("name"));
        portal.setWorldId(UUID.fromString(resultSet.getString("world_id")));
        portal.setNetworkId(UUID.fromString(resultSet.getString("network_id")));
        portal.setSortOrder(resultSet.getInt("sort_order"));
        portal.setCreatedBy(UUID.fromString(resultSet.getString("created_by")));

        try {
            portal.setCreatedAt(this.dateFormat.parse(resultSet.getString("created_at")));
        } catch (ParseException e) {
            this.logger.atSevere().log("Error parsing date for portal " + portal.getId() + ": " + e.getMessage());
        }

        return portal;
    }

    private List<PortalBound> getBoundsForPortal(UUID portalId) {
        List<PortalBound> bounds = new ArrayList<>();
        Connection connection = this.database.getConnection();
        String sql = "SELECT id, portal_id, x, y, z FROM portal_bounds WHERE portal_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, portalId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PortalBound bound = new PortalBound();
                    bound.setId(UUID.fromString(resultSet.getString("id")));
                    bound.setPortalId(UUID.fromString(resultSet.getString("portal_id")));
                    bound.setX(resultSet.getInt("x"));
                    bound.setY(resultSet.getInt("y"));
                    bound.setZ(resultSet.getInt("z"));
                    bounds.add(bound);
                }
            }
        } catch (SQLException e) {
            this.logger.atSevere().log("Error fetching bounds for portal " + portalId + ": " + e.getMessage());
        }

        return bounds;
    }

    private PortalDestination getDestinationForPortal(UUID portalId) {
        Connection connection = this.database.getConnection();
        String sql = "SELECT id, portal_id, x, y, z, body_yaw, head_yaw, head_pitch FROM portal_destinations WHERE portal_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, portalId.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PortalDestination destination = new PortalDestination();

                    destination.setId(UUID.fromString(resultSet.getString("id")));
                    destination.setPortalId(UUID.fromString(resultSet.getString("portal_id")));
                    destination.setX(resultSet.getDouble("x"));
                    destination.setY(resultSet.getDouble("y"));
                    destination.setZ(resultSet.getDouble("z"));
                    destination.setBodyYaw(resultSet.getFloat("body_yaw"));
                    destination.setHeadYaw(resultSet.getFloat("head_yaw"));
                    destination.setHeadPitch(resultSet.getFloat("head_pitch"));

                    return destination;
                }
            }
        } catch (SQLException e) {
            this.logger.atSevere().log("Error fetching destination for portal " + portalId + ": " + e.getMessage());
        }

        return null;
    }
}
