package me.pietelite.einsteinsworkshopedu.tools;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class Feature {

    public boolean isEnabled;
    private final String name;
    private final FeatureManager manager;
    private final EinsteinsWorkshopCommand[] commands;

    public Feature(EweduPlugin plugin, String name, FeatureManager manager) {
        this(plugin, name, manager, new EinsteinsWorkshopCommand[0]);
    }

    public Feature(EweduPlugin plugin, String name, FeatureManager manager, EinsteinsWorkshopCommand[] commands) {
        this.name = name;
        this.manager = manager;
        this.commands = commands;
        for (EinsteinsWorkshopCommand command : commands) {
            plugin.getCommandManager().registerCommand(command);
        }
    }

    public FeatureManager getManager() {
        return manager;
    }

    public EinsteinsWorkshopCommand[] getCommands() {
        return commands;
    }

}
