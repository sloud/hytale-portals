package network.sloud.hytale.portals.config;

import com.google.gson.*;
import com.hypixel.hytale.logger.HytaleLogger;
import network.sloud.hytale.portals.utils.FileUtils;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

@Singleton
public class PortalsConfig {

    private transient final HytaleLogger logger;
    private transient final Gson gson;

    private transient boolean initialized = false;
    private transient Path configFilePath;

    // Defaults (must not be transient to let Gson serialize them)
    private boolean debug = false;

    @Inject
    public PortalsConfig(HytaleLogger logger) {
        this.logger = logger;

        this.gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Validates the loaded JSON object against expected fields.
     *
     * @param jsonObject   The JSON object to validate.
     * @param loadedConfig The loaded configuration instance.
     * @return True if the JSON object is valid, false otherwise.
     */
    private boolean validateJsonObject(
            @NonNull JsonObject jsonObject,
            @NonNull PortalsConfig loadedConfig
    ) {
        boolean valid = true;

        if (jsonObject.has("debug")) {
            this.debug = loadedConfig.debug;
        } else {
            valid = false;
        }

        return valid;
    }

    /**
     * Initializes the configuration manager with the server root path.
     *
     * @param configPath The path to the configuration directory.
     */
    public void initialize(@NonNull Path configPath) {
        if (this.initialized) {
            return;
        }

        this.configFilePath = configPath.resolve(FileUtils.PLUGIN_CONFIG_FILE_NAME);

        try {
            FileUtils.ensureDirectory(configPath);

            if (Files.exists(this.configFilePath)) {
                this.load();
            } else {
                this.save();
            }

            this.initialized = true;
        } catch (IOException e) {
            this.logger.atSevere().withCause(e).log("Failed to initialize configuration");
        }
    }

    /**
     * Loads the configuration from disk.
     */
    public void load() {
        try (Reader reader = Files.newBufferedReader(this.configFilePath)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!element.isJsonObject()) {
                return;
            }

            JsonObject jsonObject = element.getAsJsonObject();
            PortalsConfig loaded = this.gson.fromJson(element, PortalsConfig.class);

            if (loaded == null) {
                return;
            }

            boolean isConfigValid = this.validateJsonObject(jsonObject, loaded);

            if (!isConfigValid) {
                this.save();
            }

            this.updateLoggerLevel();

            this.logger.atInfo().log("Configuration loaded from " + this.configFilePath);
        } catch (Exception e) {
            this.updateLoggerLevel();

            this.logger.atSevere().withCause(e).log("Failed to load configuration");
        }
    }

    /**
     * Updates the logger levels based on the debug setting.
     */
    private void updateLoggerLevel() {
        Level level = this.debug ? Level.ALL : Level.INFO;
        this.setLoggerLevel(level);

        this.logger.atInfo().log("Debug mode is " + (this.debug ? "enabled" : "disabled"));
    }

    private void setLoggerLevel(Level level) {
        this.logger.setLevel(level);
    }

    /**
     * Saves the current configuration to disk.
     */
    public void save() {
        try (Writer writer = Files.newBufferedWriter(this.configFilePath)) {
            this.gson.toJson(this, writer);
            this.logger.atInfo().log("Configuration saved to " + this.configFilePath);
        } catch (IOException e) {
            this.logger.atSevere().withCause(e).log("Failed to save configuration");
        }
    }

    /**
     * Reloads the configuration from disk if it exists.
     */
    public void reload() {
        if (configFilePath != null && Files.exists(configFilePath)) {
            this.logger.atInfo().log("Reloading configuration from " + this.configFilePath);
            this.load();
        }
    }

    /**
     * Checks if debug mode is enabled.
     *
     * @return True if debug mode is on.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug mode and updates loggers.
     *
     * @param debug The new debug state.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;

        this.updateLoggerLevel();
        this.save();
    }
}
