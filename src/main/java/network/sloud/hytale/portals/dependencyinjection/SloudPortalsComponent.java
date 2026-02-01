package network.sloud.hytale.portals.dependencyinjection;

import network.sloud.hytale.portals.commands.PortalCommand;
import network.sloud.hytale.portals.config.PortalsConfig;
import network.sloud.hytale.portals.database.Database;
import network.sloud.hytale.portals.systems.events.BreakBlockEventSystem;
import network.sloud.hytale.portals.systems.events.DamageBlockEventSystem;
import network.sloud.hytale.portals.systems.tick.EntryTickingSystem;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {SloudPortalsModule.class})
public interface SloudPortalsComponent {
    // Configuration
    PortalsConfig config();

    // Database
    Database database();

    // Commands
    PortalCommand portalCommand();

    // Systems - Events
    BreakBlockEventSystem breakBlockEventSystem();
    DamageBlockEventSystem damageBlockEventSystem();

    // Systems - Tick
    EntryTickingSystem entryTickingSystem();

    @Component.Builder
    interface Builder {
        SloudPortalsComponent build();
        Builder portalsModule(SloudPortalsModule sloudPortalsModule);
    }
}
