package network.sloud.hytale.portals.database;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hypixel.hytale.logger.HytaleLogger;
import network.sloud.hytale.portals.utils.FileUtils;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;

@Singleton
public class Database {

    private final HytaleLogger logger;
    private Connection connection;

    private boolean initialized = false;

    @Inject
    public Database(HytaleLogger logger) {
        this.logger = logger;
    }

    @Nullable
    public Connection getConnection() {
        return connection;
    }

    public void initialize(Path databasePath) {
        if (this.initialized) {
            return;
        }

        Path databaseFilePath = databasePath.resolve(FileUtils.PLUGIN_DATABASE_FILE_NAME);

        try {
            FileUtils.ensureDirectory(databasePath);
        } catch (IOException e) {
            this.logger.atSevere().withCause(e).log("Failed to initialize database directory");
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + databaseFilePath.toAbsolutePath()
            );

            try (Statement statement = this.connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON;");
            } finally {
                this.createTables();
            }

            this.initialized = true;
        } catch (Exception e) {
            this.logger.atSevere().withCause(e).log("Error initializing database");
        }
    }

    private void createTables() throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            // Networks table
            statement.execute("CREATE TABLE IF NOT EXISTS networks (" +
                    "id TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "created_by TEXT NOT NULL," +
                    "created_at TEXT NOT NULL" +
                    ")");

            // Portals table
            statement.execute("CREATE TABLE IF NOT EXISTS portals (" +
                    "id TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "world_id TEXT NOT NULL," +
                    "network_id TEXT NOT NULL," +
                    "sort_order INTEGER DEFAULT 0," +
                    "created_by TEXT NOT NULL," +
                    "created_at TEXT NOT NULL," +
                    "FOREIGN KEY (network_id) REFERENCES networks(id) ON DELETE CASCADE" +
                    ")");

            // Portal bounds table (composition relationship with portals)
            statement.execute("CREATE TABLE IF NOT EXISTS portal_bounds (" +
                    "id TEXT PRIMARY KEY," +
                    "portal_id TEXT NOT NULL," +
                    "x INTEGER NOT NULL," +
                    "y INTEGER NOT NULL," +
                    "z INTEGER NOT NULL," +
                    "FOREIGN KEY (portal_id) REFERENCES portals(id) ON DELETE CASCADE" +
                    ")");

            // Portal destinations table (one-to-one relationship with portals)
            statement.execute("CREATE TABLE IF NOT EXISTS portal_destinations (" +
                    "id TEXT PRIMARY KEY," +
                    "portal_id TEXT NOT NULL UNIQUE," +
                    "x REAL NOT NULL," +
                    "y REAL NOT NULL," +
                    "z REAL NOT NULL," +
                    "body_yaw REAL NOT NULL," +
                    "head_yaw REAL NOT NULL," +
                    "head_pitch REAL NOT NULL," +
                    "FOREIGN KEY (portal_id) REFERENCES portals(id) ON DELETE CASCADE" +
                    ")");
        }
    }
}
