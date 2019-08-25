package me.pietelite.einsteinsworkshopedu.features.assignments;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.TitleTooLongException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("assignment|a")
@Syntax("/ew assignment help")
@CommandPermission("einsteinsworkshop.student")
public class AssignmentCommand extends EinsteinsWorkshopCommand {

  public AssignmentCommand(EweduPlugin plugin) {
    super(plugin);
  }

  @Default
  @Subcommand("help")
  void onHelp(CommandSource source) {
    if (source.hasPermission("einsteinsworkshop.student")) {
      source.sendMessage(commandMessage("/ew assignment|a", "list"));
      source.sendMessage(commandMessage("/ew assignment|a", "complete <id>"));
    }
    if (source.hasPermission("einsteinsworkshop.instructor")) {
      source.sendMessage(commandMessage("/ew assignment|a", "add <type> <title...>"));
      source.sendMessage(commandMessage("/ew assignment|a", "info <id>"));
      source.sendMessage(commandMessage("/ew assignment|a", "remove <id>"));
      source.sendMessage(commandMessage("/ew assignment|a", "edit title <id> <title...>"));
      source.sendMessage(commandMessage("/ew assignment|a", "edit body <id> <body...>"));
      source.sendMessage(commandMessage("/ew assignment|a", "edit type <id> <type>"));
    }
  }

  @Subcommand("list")
  void onList(CommandSource source) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
        .getElements().isEmpty()) {
      source.sendMessage(Text.of(TextColors.YELLOW, "There are no assignments yet."));
      return;
    }
    source.sendMessage(Text.of(TextColors.GOLD, "-- All Assignments --"));
    for (int i = 0; i < plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
        .getElements().size(); i++) {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(i);
      if ((source instanceof Player) && assignment.getPlayersCompleted()
          .contains(((Player) source).getUniqueId())) {
        source.sendMessage(Text.of(TextStyles.STRIKETHROUGH, assignment.formatReadable(i + 1)));
      } else {
        source.sendMessage(plugin.getFeatures()
            .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
            .getElements().get(i).formatReadable(i + 1));
      }
    }
  }

  @Subcommand("complete")
  @Conditions("player")
  void onComplete(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    Player player = (Player) source;
    try {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1);
      if (assignment.getPlayersCompleted().contains(player.getUniqueId())) {
        assignment.getPlayersCompleted().remove(player.getUniqueId());
        player.sendMessage(Text.of(TextColors.GREEN, "You have marked this assignment '",
            Text.of(TextColors.YELLOW, TextStyles.BOLD, "UNCOMPLETE"),
            "'!"));

      } else {
        assignment.getPlayersCompleted().add(player.getUniqueId());
        player.sendMessage(Text.of(TextColors.GREEN, "You have marked this assignment '",
            Text.of(TextColors.GREEN, TextStyles.BOLD, "COMPLETE"),
            "'!"));
        Sponge.getServer().getBroadcastChannel().send(
            Text.of(TextColors.YELLOW,
                Text.of(TextColors.LIGHT_PURPLE, player.getName()),
                " just completed the assignment '",
                assignment.getCompletableTitle(id),
                "'!"));
      }
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  @Subcommand("add")
  @CommandPermission("einsteinsworkshop.instructor")
  @CommandCompletion("@assignment_types")
  void onAdd(CommandSource source, String type, String[] title) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      if (!EweduPlugin.getAssignmentTypes().contains(type)) {
        throw new IllegalArgumentException();
      }
      ((AssignmentManager) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager())
          .getElements().add(new Assignment(type, String.join(" ", title)));
      source.sendMessage(Text.of(TextColors.GREEN,
          "Assignment Created! Make sure to add a body message."));
      Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.YELLOW,
          "A new assignment has been created!"));
      Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.YELLOW,
          "Type '/ew a list' to view it."));
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager().save();
    } catch (IllegalArgumentException e) {
      invalidAssignmentType(source);
    } catch (TitleTooLongException e) {
      source.sendMessage(Text.of(TextColors.RED, "Titles can only be ",
                      Text.of(TextColors.GOLD, Assignment.MAXIMUM_TITLE_LENGTH),
                      " characters long."));
    }
  }

  @Subcommand("info")
  @CommandPermission("einsteinsworkshop.instructor")
  void onInfo(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      source.sendMessage(((Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1)).formatReadableVerbose(id));
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  @Subcommand("remove")
  @CommandPermission("einsteinsworkshop.instructor")
  void onRemove(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    if (plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
        .getElements().remove(id - 1) != null) {
      source.sendMessage(Text.of(TextColors.GREEN, "Assignment number ",
          Text.of(TextColors.GOLD, String.valueOf(id)),
          " was removed."));
      source.sendMessage(Text.of(TextColors.GREEN, "Assignments Renumbered."));
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager().save();
    } else {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  @Subcommand("edit title")
  @CommandPermission("einsteinsworkshop.instructor")
  void onEditTitle(CommandSource source, int id, String[] title) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1);
      assignment.setTitle(String.join(" ", title));
      assignment.setTimestamp(new Date());
      source.sendMessage(Text.of(TextColors.GREEN, "Title set!"));
      Sponge.getServer().getBroadcastChannel().send(
          Text.of(TextColors.YELLOW,
              "Assignment '",
              assignment.getCompletableTitle(id),
              "' has been edited!"));
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager().save();
    } catch (TitleTooLongException e) {
      source.sendMessage(Text.of(TextColors.RED, "Titles can only be ",
          Text.of(TextColors.GOLD, Assignment.MAXIMUM_TITLE_LENGTH),
          " characters long."));
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  @Subcommand("edit body")
  @CommandPermission("einsteinsworkshop.instructor")
  void onEditBody(CommandSource source, int id, String[] body) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1);
      assignment.setBody(String.join(" ", body));
      assignment.setTimestamp(new Date());
      source.sendMessage(Text.of(TextColors.GREEN, "Body set!"));
      Sponge.getServer().getBroadcastChannel().send(
          Text.of(TextColors.YELLOW,
              "The body of assignment '",
              assignment.getCompletableTitle(id),
              "' has been edited!"));
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager().save();
    } catch (BodyTooLongException e) {
      source.sendMessage(Text.of(TextColors.RED, "Bodies can only be ",
          Text.of(TextColors.GOLD, Assignment.MAXIMUM_BODY_LENGTH),
          " characters long."));
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  @Subcommand("edit type")
  @CommandPermission("einsteinsworkshop.instructor")
  @CommandCompletion("<id> @assignment_types")
  void onEditType(CommandSource source, int id, String type) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1);
      if (!EweduPlugin.getAssignmentTypes().contains(type)) {
        throw new IllegalArgumentException();
      }
      assignment.setType(type);
      assignment.setTimestamp(new Date());
      source.sendMessage(Text.of(TextColors.GREEN, "Type set!"));
      Sponge.getServer().getBroadcastChannel().send(
          Text.of(TextColors.YELLOW,
              "The type of assignment '",
              assignment.getCompletableTitle(id),
              "' has been edited!"));
      plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager().save();
    } catch (IllegalArgumentException e) {
      invalidAssignmentType(source);
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

  private void invalidAssignmentType(CommandSource source) {
    Text output = Text.of(TextColors.RED, "Valid assignment types are: ");
    List<Text> assignmentTypeText = new LinkedList<>();
    for (String type : EweduPlugin.getAssignmentTypes()) {
      assignmentTypeText.add(Text.of(TextColors.LIGHT_PURPLE, type));
    }
    source.sendMessage(output.concat(Text.joinWith(
        Text.of(TextColors.RED, ","),
        assignmentTypeText)));
  }

  @Subcommand("empty")
  @CommandPermission("einsteinsworkshop.instructor")
  void onEmpty(CommandSource source, int id) {
    if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.ASSIGNMENTS).isEnabled) {
      source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
      return;
    }
    try {
      Assignment assignment = (Assignment) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.ASSIGNMENTS).getManager()
          .getElements().get(id - 1);
      assignment.getPlayersCompleted().clear();
    } catch (IndexOutOfBoundsException e) {
      source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
    }
  }

}
