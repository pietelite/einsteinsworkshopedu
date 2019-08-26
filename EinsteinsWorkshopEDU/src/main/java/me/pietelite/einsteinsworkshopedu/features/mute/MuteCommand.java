package me.pietelite.einsteinsworkshopedu.features.mute;

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
@Subcommand("mute|m")
@Syntax("/ew mute help")
@CommandPermission("einsteinsworkshop.instructor")
public class MuteCommand extends EinsteinsWorkshopCommand {

  /**
   * Basic constructor.
   *
   * @param plugin The main plugin object.
   */
  public MuteCommand(EweduPlugin plugin) {
    super(plugin);
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew mute|m", "all|a"));
      source.sendMessage(commandMessage("/ew mute|m", "player|p <player>"));
    }
  }

  @Subcommand("all|a")
  void onMuteAll(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player player : Sponge.getServer().getOnlinePlayers()) {
      if (!player.hasPermission("einsteinsworkshop.immunity")) {
        if (!((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
            .getMutedPlayers().contains(player.getUniqueId())) {
          ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
              .getMutedPlayers().add(player.getUniqueId());
        }
      }
    }
    ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
        .isAllMuted = true;
    source.sendMessage(
        ClickableMessage
            .builder(Text.of(TextColors.AQUA, "All players have been muted"))
            .addClickableCommand("Unmute", "/ew um all",
                Text.of(TextColors.LIGHT_PURPLE, "Unmute all players"))
            .build()
            .getText());
  }

  @Subcommand("player|p")
  @CommandCompletion("@players")
  void onMutePlayer(CommandSource source, String name) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getName().equalsIgnoreCase(name)) {
        if (!onlinePlayer.hasPermission("einsteinsworkshop.immunity")) {
          if (!((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
              .getMutedPlayers().contains(onlinePlayer.getUniqueId())) {
            ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
                .getMutedPlayers().add(onlinePlayer.getUniqueId());
            source.sendMessage(
                ClickableMessage
                    .builder(Text.of(TextColors.AQUA, onlinePlayer.getName() + " has been muted"))
                    .addClickableCommand("Unmute", "/ew um p " + onlinePlayer.getName(),
                        Text.of(TextColors.LIGHT_PURPLE, "Unmute " + onlinePlayer.getName()))
                    .addClickableCommand("Unmute All", "/ew um a",
                        Text.of(TextColors.LIGHT_PURPLE, "Unmute all players"))
                    .build()
                    .getText());
          } else {
            source.sendMessage(Text.of(TextColors.RED, "That player is already muted!"));
          }
        } else {
          source.sendMessage(Text.of(TextColors.RED, "This player cannot be muted!"));
        }
        return;
      }
    }
    source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
  }
}