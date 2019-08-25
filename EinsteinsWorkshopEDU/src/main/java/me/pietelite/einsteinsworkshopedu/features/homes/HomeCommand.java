package me.pietelite.einsteinsworkshopedu.features.homes;

import co.aikar.commands.annotation.*;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Syntax;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("home|h")
@Syntax("/ew home help")
@CommandPermission("einsteinsworkshop.student")
public class HomeCommand extends EinsteinsWorkshopCommand {

  public HomeCommand(EweduPlugin plugin) {
    super(plugin);
  }

  @Default
  @Conditions("player")
  public void onDefault(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    Home home = ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).getHome(player);
    if (home == null) {
      player.sendMessage(Text.of(TextColors.RED, "You have not yet set a home! Use /ew home set"));
    } else {
      home.teleport(player);
      player.sendMessage(Text.of(TextColors.GREEN, "Welcome home!"));
    }
  }

  @Subcommand("help")
  public void onHelp(CommandSource source) {
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
  public void onSet(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).removeHome(player);
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).addHome(player);
    plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager().save();
    player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
  }

  @Subcommand("set")
  @Conditions("player")
  @CommandPermission("einsteinsworkshop.instructor")
  public void onSetLocation(CommandSource source, int x, int y, int z) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).removeHome(player);
    ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).addHome(player, x, y, z);
    plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager().save();
    player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
  }

  @Subcommand("player")
  @CommandCompletion("players")
  @Conditions("player")
  @CommandPermission("einsteinsworkshop.instructor")
  public void onPlayer(CommandSource source, String username) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    if (Sponge.getServer().getPlayer(username).isPresent()) {
      Player targetPlayer = Sponge.getServer().getPlayer(username).get();
      Home home = ((HomeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.HOMES).getManager()).getHome(targetPlayer);
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
