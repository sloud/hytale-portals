package dev.joeyaurel.hytale.portals.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static final String PLUGIN_DIRECTORY_NAME = "Portals";

    public static final String PLUGIN_CONFIG_FILE_NAME = "config.json";
    public static final String PLUGIN_DATABASE_FILE_NAME = "database.db";

    public static void ensureDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
