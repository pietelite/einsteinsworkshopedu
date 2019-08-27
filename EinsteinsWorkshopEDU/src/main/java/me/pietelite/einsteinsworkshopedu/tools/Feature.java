package me.pietelite.einsteinsworkshopedu.tools;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class Feature {

  public boolean isEnabled;
  private final FeatureManager manager;
  private final EinsteinsWorkshopCommand[] commands;

  public Feature(EweduPlugin plugin, FeatureManager manager) {
    this(plugin, manager, new EinsteinsWorkshopCommand[0]);
  }

  /**
   * Constructs a new Feature object which houses functional objects to
   * handle the feature during runtime.
   *
   * @param plugin   The instance of the current plugin
   * @param manager  The managing class for this feature, extended from
   *                 FeatureManager
   * @param commands A list of objects to contain behavior of the commands
   *                 associated with this feature. These are to extend from
   *                 EinsteinsWorkshopCommand.
   */
  public Feature(EweduPlugin plugin, FeatureManager manager, EinsteinsWorkshopCommand[] commands) {
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
