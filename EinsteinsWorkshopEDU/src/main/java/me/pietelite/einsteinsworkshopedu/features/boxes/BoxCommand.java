package me.pietelite.einsteinsworkshopedu.features.boxes;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

import com.flowpowered.math.vector.Vector3d;

import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;

import me.pietelite.einsteinsworkshopedu.tools.chat.ClickableMessage;
import me.pietelite.einsteinsworkshopedu.tools.chat.Menu;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("box|b")
@Syntax("/ew box help")
@CommandPermission("einsteinsworkshop.instructor")
public class BoxCommand extends EinsteinsWorkshopCommand {

  /**
   * Generates an handler for the <i>boxes</i> command.
   *
   * @param plugin The primary instance of the EinsteinsWorkshopEDU plugin
   */
  public BoxCommand(EweduPlugin plugin) {
    super(plugin,
        new Menu.Section(
            "Boxes",
            EweduPlugin.Permissions.INSTRUCTOR,
            Arrays.asList(
                new ClickableMessage.ClickableCommand(
                    "Help",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew box help",
                    "List all commands"
                ),
                new ClickableMessage.ClickableCommand(
                    "List",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew box list",
                    "List all boxes"
                ),
                new ClickableMessage.ClickableCommand(
                    "Wand",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew box wand",
                    "Get the Boxes wand"
                ),
                new ClickableMessage.ClickableCommand(
                    "Create",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew box create",
                    "Create a box with the designated selection"
                )
            )
    ));
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew box|b", "list"));
      source.sendMessage(commandMessage("/ew box|b", "position1|pos1"));
      source.sendMessage(commandMessage("/ew box|b", "position2|pos2"));
      source.sendMessage(commandMessage("/ew box|b", "create"));
      source.sendMessage(commandMessage("/ew box|b", "destroy <id>"));
      source.sendMessage(commandMessage("/ew box|b", "info [id]"));
      source.sendMessage(commandMessage("/ew box|b", "edit movement <id> true|false"));
      source.sendMessage(commandMessage("/ew box|b", "edit building <id> true|false"));
      source.sendMessage(commandMessage("/ew box|b", "show <id>"));
      source.sendMessage(commandMessage("/ew box|b", "show all"));
      source.sendMessage(commandMessage("/ew box|b", "teleport|tp <id>"));
      source.sendMessage(commandMessage("/ew box|b", "wand"));
    }
  }

  @Subcommand("list")
  void onList(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
        .getElements().isEmpty()) {
      source.sendMessage(Text.of(TextColors.YELLOW, "There are no boxes yet."));
      return;
    }
    source.sendMessage(Text.of(TextColors.GOLD, "-- All Boxes --"));
    for (int i = 0; i < plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
        .getElements().size(); i++) {
      source.sendMessage(plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(i).formatReadable(i + 1));
    }
  }

  @Subcommand("position1|pos1")
  @Conditions("player")
  void onPosition1(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    onPosition(source, Box.Position.POSITION1);
    source.sendMessage(Text.of(TextColors.GREEN, "Position 1 set!"));
  }

  @Subcommand("position2|pos2")
  @Conditions("player")
  void onPosition2(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    onPosition(source, Box.Position.POSITION2);
    source.sendMessage(Text.of(TextColors.GREEN, "Position 2 set!"));
  }

  private void onPosition(CommandSource source, @Nonnull Box.Position position) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    if (position == Box.Position.POSITION1) {
      ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
          .getPosition1Map().put(player.getUniqueId(), player.getLocation());
    } else {
      ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
          .getPosition2Map().put(player.getUniqueId(), player.getLocation());
    }
  }

  @Subcommand("create")
  @Conditions("player")
  void onCreate(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    if (!((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
        .hasSelection(player.getUniqueId())) {
      player.sendMessage(Text.of(TextColors.RED, "You don't have a selection!"));
      return;
    }
    Location<World> position1 = ((BoxManager) plugin.getFeatures()
        .get(EweduPlugin.FeatureTitle.BOXES).getManager())
        .getPosition1Map().get(player.getUniqueId());
    Location<World> position2 = ((BoxManager) plugin.getFeatures()
        .get(EweduPlugin.FeatureTitle.BOXES).getManager())
        .getPosition2Map().get(player.getUniqueId());
    if (!position1.getExtent().equals(position2.getExtent())) {
      player.sendMessage(Text.of(TextColors.RED,
          "Your two selection points must be in the same world."));
    } else {
      Box newBox = new Box(
          position1.getBlockPosition(),
          position2.getBlockPosition(),
          position1.getExtent());
      for (Box box : ((BoxManager) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.BOXES).getManager())
          .getElements()) {
        if (box.isOverlapping(newBox)) {
          player.sendMessage(Text.of(TextColors.RED,
              "Your selection cannot overlap with another box."));
          return;
        }
      }
      ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
          .getElements().add(newBox);
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager().save();
      player.sendMessage(Text.of(TextColors.GREEN, "Box added!"));
    }

  }

  @Subcommand("destroy")
  void onDestroy(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().remove(id - 1);
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager().save();
      player.sendMessage(Text.of(
          TextColors.GREEN, "Box ",
          TextColors.GOLD, id,
          TextColors.GREEN, " destroyed!"));
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("info")
  void onInfo(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      player.sendMessage(((Box) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(id - 1)).formatReadableVerbose(id));
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("info")
  @Conditions("player")
  void onInfo(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    for (int i = 0; i < plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
        .getElements().size(); i++) {
      if (((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(i)).contains(player)) {
        onInfo(player, i + 1);
        return;
      }
    }
    player.sendMessage(Text.of(TextColors.YELLOW, "You aren't in a box."));
  }

  @Subcommand("edit movement")
  @CommandPermission("einsteinsworkshop.instructor")
  void onEditMovement(CommandSource source, int id, boolean value) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      if (((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(id - 1)).movement == value) {
        player.sendMessage(Text.of(
            TextColors.YELLOW, "Student mobility was already set to ",
            TextColors.DARK_PURPLE, value,
            TextColors.YELLOW, "!"));
      } else {
        ((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
            .getElements().get(id - 1)).movement = value;
        plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager().save();
        player.sendMessage(Text.of(TextColors.GREEN, "Student mobility set!"));
      }
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("edit building")
  void onEditBuilding(CommandSource source, int id, boolean value) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      if (((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(id - 1)).building == value) {
        player.sendMessage(Text.of(
            TextColors.YELLOW, "Student building ability was already set to ",
            TextColors.DARK_PURPLE, value,
            TextColors.YELLOW, "!"));
      } else {
        ((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
            .getElements().get(id - 1)).building = value;
        plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager().save();
        player.sendMessage(Text.of(TextColors.GREEN, "Student building ability set!"));
      }
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("show")
  @Conditions("player")
  void onShow(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      ((Box) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(id - 1)).spawnBorderParticles(player, Color.GREEN);
      player.sendMessage(Text.of(
          TextColors.GREEN, "Showing box ",
          TextColors.GOLD, id,
          TextColors.GREEN, "."));
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("show all")
  @Conditions("player")
  void onShowAll(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    for (Box box : ((BoxManager) plugin.getFeatures()
        .get(EweduPlugin.FeatureTitle.BOXES).getManager())
        .getElements()) {
      box.spawnBorderParticles(player, Color.GREEN);
    }
    player.sendMessage(Text.of(TextColors.GREEN, "Showing all boxes"));
  }

  @Subcommand("teleport|tp")
  @Conditions("player")
  void onTeleport(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      Box box = (Box) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.BOXES).getManager()
          .getElements().get(id - 1);
      if (!player.setTransformSafely(new Transform<>(
          player.getWorld(),
          new Vector3d(box.getXMin(), box.getYMin(), box.getZMin()),
          player.getRotation()
      ))) {
        player.sendMessage(Text.of(TextColors.RED, "No safe place to go to!"));
        return;
      }
      player.sendMessage(Text.of(
          TextColors.GREEN, "Teleported to box ",
          TextColors.GOLD, id,
          TextColors.GREEN, "."));
    } catch (IndexOutOfBoundsException e) {
      player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
    }
  }

  @Subcommand("wand")
  @Conditions("player")
  void onWand(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    ((Player) source).setItemInHand(
        HandTypes.MAIN_HAND,
        ItemStack.of(((BoxManager) plugin.getFeatures()
            .get(EweduPlugin.FeatureTitle.BOXES).getManager())
            .getWandItem(), 1));
    source.sendMessage(Text.of(TextColors.GREEN, "Here you go!"));
  }

}
