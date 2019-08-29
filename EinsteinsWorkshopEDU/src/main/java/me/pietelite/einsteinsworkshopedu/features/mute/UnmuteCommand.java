package me.pietelite.einsteinsworkshopedu.features.mute;

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
@Subcommand("unmute|um")
@Syntax("/ew unmute help")
@CommandPermission("einsteinsworkshop.instructor")
public class UnmuteCommand extends EinsteinsWorkshopCommand {

  /**
   * Generates an handler for the <i>unmute</i> command.
   *
   * @param plugin The primary instance of the EinsteinsWorkshopEDU plugin
   */
  public UnmuteCommand(EweduPlugin plugin) {
    super(plugin,
        new Menu.Section(
            "Unmute",
            EweduPlugin.Permissions.INSTRUCTOR,
            Arrays.asList(
                new ClickableMessage.ClickableCommand(
                    "Help",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew unmute help",
                    "List all commands"
                ),
                new ClickableMessage.ClickableCommand(
                    "Unmute All",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew unmute all",
                    "Unmute all students"
                )
            ))
    );
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew unmute|um", "all|a"));
      source.sendMessage(commandMessage("/ew unmute|m", "player|p <player>"));
    }
  }

  @Subcommand("all|a")
  void onMuteAll(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player player : Sponge.getServer().getOnlinePlayers()) {
      ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
          .getMutedPlayers().remove(player.getUniqueId());
    }
    ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
        .isAllMuted = false;
    source.sendMessage(
        ClickableMessage
            .builder(Text.of(TextColors.GREEN, "All players have been unmuted"))
            .addClickableCommand("Mute", "/ew m all",
                Text.of(TextColors.LIGHT_PURPLE, "Mute all players"))
            .build()
            .toText());
  }

  @Subcommand("player|p")
  @CommandCompletion("@players")
  void onMutePlayer(CommandSource source, String name) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
        if (((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
            .getMutedPlayers().remove(onlinePlayer.getUniqueId())) {
          source.sendMessage(
              ClickableMessage
                  .builder(Text.of(TextColors.GREEN, onlinePlayer.getName() + " has been unmuted"))
                  .addClickableCommand("Mute", "/ew m p " + onlinePlayer.getName(),
                      Text.of(TextColors.LIGHT_PURPLE, "Mute " + onlinePlayer.getName()))
                  .addClickableCommand("Mute All", "/ew m a",
                      Text.of(TextColors.LIGHT_PURPLE, "Mute all players"))
                  .build()
                  .toText());
        } else {
          source.sendMessage(Text.of(TextColors.RED, "That player is not muted!"));
        }
        return;
      }
    }
    source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
  }
}