package me.pietelite.einsteinsworkshopedu.features.homes;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
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
@Subcommand("home|h")
@Syntax("/ew home help")
@CommandPermission("einsteinsworkshop.student")
public class HomeCommand extends EinsteinsWorkshopCommand {

  /**
   * Generates an handler for the <i>home</i> command.
   *
   * @param plugin The primary instance of the EinsteinsWorkshopEDU plugin
   */
  public HomeCommand(EweduPlugin plugin) {
    super(plugin,
        new Menu.Section(
            "Freeze",
            EweduPlugin.Permissions.STUDENT,
            Arrays.asList(
                new ClickableMessage.ClickableCommand(
                    "Help",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew home help",
                    "List all commands"
                ),
                new ClickableMessage.ClickableCommand(
                    "Go Home",
                    EweduPlugin.Permissions.STUDENT,
                    "/ew home",
                    "Teleport yourself to your own home"
                ),
                new ClickableMessage.ClickableCommand(
                    "Set Home",
                    EweduPlugin.Permissions.STUDENT,
                    "/ew home set",
                    "Set your home where you are"
                )
            )
    ));
  }

  @Default
  @Conditions("player")
  void onDefault(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    Home home = ((HomeManager) plugin.getFeatures()
        .get(EweduPlugin.FeatureTitle.HOMES).getManager())
        .getHome(player);
    if (home == null) {
      player.sendMessage(Text.of(TextColors.RED, "You have not yet set a home! Use /ew home set"));
    } else {
      home.teleport(player);
      player.sendMessage(Text.of(TextColors.GREEN, "Welcome home!"));
    }
  }

  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.student")) {
      source.sendMessage(commandMessage("/ew home|h", ""));
      source.sendMessage(commandMessage("/ew home|h", "set"));
    }
    if (source.hasPermission("einsteinsworkshop.student")) {
      source.sendMessage(commandMessage("/ew home|h", "set <x> <y> <z>"));
      source.sendMessage(commandMessage("/ew home|h", "player <player>"));
    }
  }

  @Subcommand("set")
  @Conditions("player")
  void onSet(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager())
        .removeHome(player);
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager())
        .addHome(player);
    plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager().save();
    player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
  }

  @Subcommand("set")
  @Conditions("player")
  @CommandPermission("einsteinsworkshop.instructor")
  void onSetLocation(CommandSource source, int x, int y, int z) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager())
        .removeHome(player);
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager())
        .addHome(player, x, y, z);
    plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager().save();
    player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
  }

  @Subcommand("player")
  @CommandCompletion("players")
  @Conditions("player")
  @CommandPermission("einsteinsworkshop.instructor")
  void onPlayer(CommandSource source, String username) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    if (Sponge.getServer().getPlayer(username).isPresent()) {
      Player targetPlayer = Sponge.getServer().getPlayer(username).get();
      Home home = ((HomeManager) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.HOMES).getManager())
          .getHome(targetPlayer);
      if (home == null) {
        player.sendMessage(Text.of(TextColors.RED, "That player has not yet set a home!"));
      } else {
        if (home.teleport(player)) {
          player.sendMessage(Text.of(
              TextColors.GREEN, "Teleported to ",
              TextColors.LIGHT_PURPLE, targetPlayer.getName(),
              TextColors.GREEN, "'s home."));
        } else {
          player.sendMessage(Text.of(TextColors.RED, "No safe place to teleport!"));
        }
      }
    } else {
      player.sendMessage(Text.of(TextColors.RED, "That player could not be found!"));
    }
  }
}
