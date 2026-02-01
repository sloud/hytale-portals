package network.sloud.hytale.portals.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static final String PLUGIN_DIRECTORY_NAME_OLD = "Portals";
    public static final String PLUGIN_DIRECTORY_NAME = "SloudPortals";

    public static final String PLUGIN_CONFIG_FILE_NAME = "config.json";
    public static final String PLUGIN_DATABASE_FILE_NAME = "database.db";

    public static void ensureDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void migrateOldDirectory(Path oldPath, Path newPath) throws IOException {
        if (!Files.exists(oldPath) || !Files.isDirectory(oldPath)) {
            return;
        }

        try (java.util.stream.Stream<Path> stream = Files.list(oldPath)) {
            if (stream.count() != 2) {
                return;
            }
        }

        if (!Files.exists(oldPath.resolve("config.json")) ||
                !Files.exists(oldPath.resolve("database.db"))) {
            return;
        }

        if (Files.exists(newPath)) {
            return;
        }

        Files.move(oldPath, newPath);
    }
}
