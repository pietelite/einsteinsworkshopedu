package me.pietelite.einsteinsworkshopedu.features.freeze;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.chat.ClickableMessage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("freeze|f")
@Syntax("/ew freeze help")
@CommandPermission("einsteinsworkshop.instructor")
public class FreezeCommand extends EinsteinsWorkshopCommand {

  /**
   * Basic constructor.
   *
   * @param plugin The main plugin object.
   */
  public FreezeCommand(EweduPlugin plugin) {
    super(plugin);
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew freeze|f", "all|a"));
      source.sendMessage(commandMessage("/ew freeze|f", "player|p <player>"));
    }
  }

  @Subcommand("all|a")
  void onFreezeAll(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player player : Sponge.getServer().getOnlinePlayers()) {
      if (!player.hasPermission("einsteinsworkshop.immunity")) {
        if (!((FreezeManager) plugin.getFeatures()
            .get(EweduPlugin.FeatureTitle.FREEZE).getManager())
            .getFrozenPlayers().contains(player.getUniqueId())) {
          ((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
              .getFrozenPlayers().add(player.getUniqueId());
          player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
        }
      }
    }
    ((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
        .isAllFrozen = true;
    source.sendMessage(
        ClickableMessage
            .builder(Text.of(TextColors.AQUA, "All players have been frozen"))
            .addClickableCommand("Unfreeze", "/ew uf all",
                Text.of(TextColors.LIGHT_PURPLE, "Unfreeze all players"))
            .build()
            .getText());
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
        if (!onlinePlayer.hasPermission("einsteinsworkshop.immunity")) {
          if (!((FreezeManager) plugin.getFeatures()
              .get(EweduPlugin.FeatureTitle.FREEZE).getManager())
              .getFrozenPlayers().contains(onlinePlayer.getUniqueId())) {
            ((FreezeManager) plugin.getFeatures()
                .get(EweduPlugin.FeatureTitle.FREEZE).getManager())
                .getFrozenPlayers().add(onlinePlayer.getUniqueId());
            source.sendMessage(
                ClickableMessage
                    .builder(Text.of(TextColors.AQUA, onlinePlayer.getName() + " has been frozen"))
                    .addClickableCommand("Unfreeze", "/ew uf p " + onlinePlayer.getName(),
                        Text.of(TextColors.LIGHT_PURPLE, "Unfreeze " + onlinePlayer.getName()))
                    .addClickableCommand("Unfreeze All", "/ew uf a",
                        Text.of(TextColors.LIGHT_PURPLE, "Unfreeze all players"))
                    .build()
                    .getText());
            onlinePlayer.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
          } else {
            source.sendMessage(Text.of(TextColors.RED, "That player is already frozen!"));
          }
        } else {
          source.sendMessage(Text.of(TextColors.RED, "This player cannot be frozen!"));
        }
        return;
      }
    }
    source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
  }
}
