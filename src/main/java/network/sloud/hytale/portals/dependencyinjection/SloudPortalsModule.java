package network.sloud.hytale.portals.dependencyinjection;

import com.hypixel.hytale.logger.HytaleLogger;
import network.sloud.hytale.portals.SloudPortalsPlugin;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class SloudPortalsModule {

    private final HytaleLogger logger;
    private final SloudPortalsPlugin plugin;

    public SloudPortalsModule(HytaleLogger logger, SloudPortalsPlugin plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    public HytaleLogger provideLogger() {
        return this.logger;
    }

    @Provides
    @Singleton
    public SloudPortalsPlugin providePlugin() {
        return this.plugin;
    }
}
