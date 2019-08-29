package me.pietelite.einsteinsworkshopedu.features.freeze;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

import java.util.Arrays;
import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.chat.ClickableMessage;
import me.pietelite.einsteinsworkshopedu.tools.chat.Menu;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("unfreeze|uf")
@Syntax("/ew unfreeze all|player")
@CommandPermission("einsteinsworkshop.instructor")
public class UnfreezeCommand extends EinsteinsWorkshopCommand {

  /**
   * Generates an handler for the <i>unfreeze</i> command.
   *
   * @param plugin The primary instance of the EinsteinsWorkshopEDU plugin
   */
  public UnfreezeCommand(EweduPlugin plugin) {
    super(plugin,
        new Menu.Section(
            "Unfreeze",
            EweduPlugin.Permissions.INSTRUCTOR,
            Arrays.asList(
                new ClickableMessage.ClickableCommand(
                    "Help",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew unfreeze help",
                    "List all commands"
                ),
                new ClickableMessage.ClickableCommand(
                    "Unfreeze All",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew unfreeze all",
                    "Unfreeze all students"
                )
            ))
    );
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew unfreeze|uf", "all|a"));
      source.sendMessage(commandMessage("/ew unfreeze|uf", "player|p <player>"));
    }
  }

  @Subcommand("all|a")
  void onFreezeAll(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player player : Sponge.getServer().getOnlinePlayers()) {
      if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
          .getFrozenPlayers().remove(player.getUniqueId())) {
        player.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
      }
    }
    ((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
        .isAllFrozen = false;
    source.sendMessage(
        ClickableMessage
            .builder(Text.of(TextColors.GREEN, "All players have been unfrozen"))
            .addClickableCommand("Freeze", "/ew f all",
                Text.of(TextColors.LIGHT_PURPLE, "Freeze all players"))
            .build()
            .toText());
  }

  @Subcommand("player|p")
  @CommandCompletion("@players")
  void onFreezePlayer(CommandSource source, String name) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
        if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
            .getFrozenPlayers().remove(onlinePlayer.getUniqueId())) {
          source.sendMessage(
              ClickableMessage
                  .builder(Text.of(TextColors.GREEN, onlinePlayer.getName() + " has been unfrozen"))
                  .addClickableCommand("Freeze", "/ew f p " + onlinePlayer.getName(),
                      Text.of(TextColors.LIGHT_PURPLE, "Freeze " + onlinePlayer.getName()))
                  .addClickableCommand("Freeze All", "/ew uf a",
                      Text.of(TextColors.LIGHT_PURPLE, "Freeze all players"))
                  .build()
                  .toText());
          onlinePlayer.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
          return;
        } else {
          source.sendMessage(Text.of(TextColors.RED, "That player wasn't frozen!"));
        }
        return;
      }
    }
    source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
  }

}
