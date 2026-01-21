package dev.joeyaurel.hytale.portals.utils;

import com.hypixel.hytale.server.core.Constants;

import java.io.File;

public class FileUtils {
    public static String MAIN_PATH = Constants.UNIVERSE_PATH.resolve("Portals").toAbsolutePath().toString();
    public static String DATABASE_PATH = MAIN_PATH + File.separator + "database.db";
}
