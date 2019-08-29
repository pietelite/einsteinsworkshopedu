package me.pietelite.einsteinsworkshopedu;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import me.pietelite.einsteinsworkshopedu.tools.Feature;
import me.pietelite.einsteinsworkshopedu.tools.chat.Menu;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@CommandAlias("einsteinsworkshop|ew")
public class EinsteinsWorkshopCommand extends BaseCommand {

  /**
   * The main plugin object.
   */
  protected final EweduPlugin plugin;
  private final Menu.Section menuSection;

  /**
   * Basic constructor.
   *
   * @param plugin The main plugin object.
   */
  public EinsteinsWorkshopCommand(EweduPlugin plugin, Menu.Section menuSection) {
    this.plugin = plugin;
    this.menuSection = menuSection;
  }

  @Default
  @HelpCommand
  @Conditions("player")
  void onHelp(CommandSource source, CommandHelp help) {
    if (source instanceof Player) {
      Player player = (Player) source;
      Menu menu = new Menu();
      for (Feature feature : plugin.getFeatures().values()) {
        for (EinsteinsWorkshopCommand command : feature.getCommands()) {
          menu.addSection(command.getMenuSection());
        }
      }
      menu.sendTo(player);
    }
  }

  protected static Text commandMessage(String command, String subcommand) {
    return Text.builder(command).color(TextColors.GRAY)
        .append(Text.of(" "))
        .append(Text.of(TextColors.AQUA, subcommand))
        .build();
  }

  private Menu.Section getMenuSection() {
    return menuSection;
  }
}
