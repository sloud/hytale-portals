package dev.joeyaurel.hytale.portals;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.joeyaurel.hytale.portals.dependencyinjection.DaggerPortalsComponent;
import dev.joeyaurel.hytale.portals.dependencyinjection.PortalsComponent;
import dev.joeyaurel.hytale.portals.dependencyinjection.PortalsModule;
import dev.joeyaurel.hytale.portals.utils.FileUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class PortalsPlugin extends JavaPlugin {

    private final HytaleLogger logger = HytaleLogger.forEnclosingClass();

    private final String pluginName;
    private final String pluginVersion;

    private final PortalsComponent component;

    public PortalsPlugin(@Nonnull JavaPluginInit init) {
        super(init);

        this.pluginName = this.getManifest().getName();
        this.pluginVersion = this.getManifest().getVersion().toString();

        this.component = DaggerPortalsComponent.builder()
                .portalsModule(new PortalsModule(this.logger, this))
                .build();

        Path serverRootPath = Paths.get(".").toAbsolutePath().normalize();
        Path pluginConfigPath = serverRootPath.resolve("mods").resolve(FileUtils.PLUGIN_DIRECTORY_NAME);

        try {
            FileUtils.ensureDirectory(pluginConfigPath);
        } catch (IOException e) {
            this.logger.atSevere().withCause(e).log("Failed to ensure directory exists: " + pluginConfigPath);
        }

        // Initialize configuration
        this.component.config().initialize(pluginConfigPath);

        // Initialize database
        this.component.database().initialize(pluginConfigPath);

        this.logger.atInfo().log("Hello from " + this.pluginName + " version " + this.pluginVersion + "!");
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public PortalsComponent getComponent() {
        return component;
    }

    @Override
    protected void start() {
        this.logger.atInfo().log("Setting up plugin " + this.pluginName + " version " + this.pluginVersion + "...");

        // Uncomment the following when developing
        //CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "auth persistence Encrypted");
        //CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "auth login device");

        this.registerSystems();
        this.registerCommands();

        this.logger.atInfo().log("Plugin " + this.pluginName + " setup successfully!");
    }

    @Override
    protected void shutdown() {
        this.logger.atInfo().log("Shutting down plugin " + this.pluginName + "...");

        Connection databaseConnection = this.component.database().getConnection();

        if (databaseConnection != null) {
            try {
                databaseConnection.close();
                this.logger.atInfo().log("Database connection closed successfully.");
            } catch (SQLException e) {
                this.logger.atWarning().withCause(e).log("Failed to close database connection");
            }
        }
    }

    private void registerSystems() {
        this.logger.atFine().log("Registering systems...");

        // Events
        this.getEntityStoreRegistry().registerSystem(this.component.breakBlockEventSystem());
        this.getEntityStoreRegistry().registerSystem(this.component.damageBlockEventSystem());

        // Ticks
        this.getEntityStoreRegistry().registerSystem(this.component.entryTickingSystem());
    }

    private void registerCommands() {
        this.logger.atFine().log("Registering commands...");

        this.getCommandRegistry().registerCommand(this.component.portalCommand());
    }
}
