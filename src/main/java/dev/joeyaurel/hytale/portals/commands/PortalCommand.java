package dev.joeyaurel.hytale.portals.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import dev.joeyaurel.hytale.portals.commands.portal.PortalCreateCommand;

import javax.annotation.Nonnull;

@Singleton
public class PortalCommand extends CommandBase {

    private final String pluginName;
    private final String pluginVersion;

    @Inject
    public PortalCommand(PortalCreateCommand portalCreateCommand, String pluginName, String pluginVersion) {
        super("portal", "Prints basic infos for the " + pluginName + " plugin.");

        this.addAliases("portals");

        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;

        this.addSubCommand(portalCreateCommand);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello from the " + pluginName + " v" + pluginVersion + " plugin!"));
    }
}
