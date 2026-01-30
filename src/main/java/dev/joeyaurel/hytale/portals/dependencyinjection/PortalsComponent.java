package dev.joeyaurel.hytale.portals.dependencyinjection;

import dev.joeyaurel.hytale.portals.commands.PortalCommand;
import dev.joeyaurel.hytale.portals.config.PortalsConfig;
import dev.joeyaurel.hytale.portals.database.Database;
import dev.joeyaurel.hytale.portals.systems.events.BreakBlockEventSystem;
import dev.joeyaurel.hytale.portals.systems.events.DamageBlockEventSystem;
import dev.joeyaurel.hytale.portals.systems.tick.EntryTickingSystem;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {PortalsModule.class})
public interface PortalsComponent {
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
        PortalsComponent build();
        Builder portalsModule(PortalsModule portalsModule);
    }
}
