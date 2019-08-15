package me.pietelite.einsteinsworkshopedu.features.assignments;

import java.util.Date;

import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.TitleTooLongException;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("assignment|a")
@Syntax("/ew assignment")
@CommandPermission("einsteinsworkshop.student")
public class AssignmentCommand extends EinsteinsWorkshopCommand {
	
	public AssignmentCommand(EweduPlugin plugin) {
		super(plugin);
		addSubCommand(new SubCommand("einsteinsworkshop.student", "/ew assignment list"));
		addSubCommand(new SubCommand("einsteinsworkshop.student", "/ew assignment complete"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment add <" +
				String.join("|", EweduPlugin.getAssignmentTypes()) + "> <title...>"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment info <id>"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment remove <id>"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment edit title <id> <title...>"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment edit body <id> <body...>"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew assignment edit type <id> <" +
				String.join("|", EweduPlugin.getAssignmentTypes()) + ">"));
	}
		
	@Subcommand("list")
	public void onList(CommandSource source) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		if (plugin.getAssignmentManager().getElements().isEmpty()) {
			source.sendMessage(Text.of(TextColors.YELLOW, "There are no assignments yet."));
			return;
		}
		source.sendMessage(Text.of(TextColors.GOLD, "-- All Assignments --"));
		for (int i = 0; i < plugin.getAssignmentManager().getElements().size(); i++) {
			Assignment assignment = plugin.getAssignmentManager().getElements().get(i);
			if ((source instanceof Player) && assignment.getPlayersCompleted().contains((Player) source)) {
				source.sendMessage(Text.of(TextStyles.STRIKETHROUGH, assignment.formatReadable(i + 1)));
			} else {
				source.sendMessage(plugin.getAssignmentManager().getElements().get(i).formatReadable(i + 1));
			}
		}
	}
	
	@Subcommand("complete")
	@Conditions("player")
	public void onComplete(CommandSource source, int id) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		Player player = (Player) source;
		try {
			Assignment assignment = plugin.getAssignmentManager().getElements().get(id - 1);
			if (assignment.getPlayersCompleted().contains(player)) {
				assignment.getPlayersCompleted().remove(player);
				player.sendMessage(Text.of(TextColors.GREEN, "You have marked this assignment '", 
						Text.of(TextColors.YELLOW, TextStyles.BOLD, "UNCOMPLETE"),
						"'!"));
				
			} else {
				assignment.getPlayersCompleted().add(player);
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
	public void onAdd(CommandSource source, String type, String[] title) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		try {
			if (!EweduPlugin.getAssignmentTypes().contains(type)) throw new IllegalArgumentException();
			plugin.getAssignmentManager().getElements().add(new Assignment(type, String.join(" ", title)));
			source.sendMessage(Text.of(TextColors.GREEN, "Assignment Created! Make sure to add a body message."));
			Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.YELLOW, "A new assignment has been created!"));
			Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.YELLOW, "Type '/ew a list' to view it."));
			plugin.getAssignmentManager().save();
		} catch (IllegalArgumentException e) {
			source.sendMessage(Text.of(TextColors.RED, "That is not a valid assignment type."));
		} catch (TitleTooLongException e) {
			source.sendMessage(Text.of(TextColors.RED, "Titles can only be ", 
											Text.of(TextColors.GOLD, Assignment.MAXIMUM_TITLE_LENGTH),
											" characters long."));
		}
	}
	
	@Subcommand("info")
	@CommandPermission("einsteinsworkshop.instructor")
	public void onInfo(CommandSource source, int id) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		try {
			source.sendMessage(plugin.getAssignmentManager().getElements().get(id - 1).formatReadableVerbose(id));
		} catch (IndexOutOfBoundsException e) {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
	}
	
	@Subcommand("remove")
	@CommandPermission("einsteinsworkshop.instructor")
	public void onRemove(CommandSource source, int id) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		if (plugin.getAssignmentManager().getElements().remove(id - 1) != null) {
			source.sendMessage(Text.of(TextColors.GREEN, "Assignment number ",
					Text.of(TextColors.GOLD, String.valueOf(id)),
					" was removed."));
			source.sendMessage(Text.of(TextColors.GREEN, "Assignments Renumbered."));
			plugin.getAssignmentManager().save();
		} else {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}	
	}
	
	@Subcommand("edit title")
	@CommandPermission("einsteinsworkshop.instructor")
	public void onEditTitle(CommandSource source, int id, String[] title) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		try {
			Assignment assignment = plugin.getAssignmentManager().getElements().get(id - 1);
			assignment.setTitle(String.join(" ", title));
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Title set!"));
			Sponge.getServer().getBroadcastChannel().send(
					Text.of(TextColors.YELLOW, "Assignment '", assignment.getCompletableTitle(id), "' has been edited!"));
			plugin.getAssignmentManager().save();
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
	public void onEditBody(CommandSource source, int id, String[] body) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		try {
			Assignment assignment = plugin.getAssignmentManager().getElements().get(id - 1);
			assignment.setBody(String.join(" ", body));
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Body set!"));
			Sponge.getServer().getBroadcastChannel().send(
					Text.of(TextColors.YELLOW, "The body of assignment '", assignment.getCompletableTitle(id), "' has been edited!"));
			plugin.getAssignmentManager().save();
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
	public void onEditType(CommandSource source, int id, String type) {
		if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.ASSIGNMENTS).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
		try {
			Assignment assignment = plugin.getAssignmentManager().getElements().get(id - 1);
			if (!EweduPlugin.getAssignmentTypes().contains(type)) throw new IllegalArgumentException();
			assignment.setType(type);
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Type set!"));
			Sponge.getServer().getBroadcastChannel().send(
					Text.of(TextColors.YELLOW, "The type of assignment '", assignment.getCompletableTitle(id), "' has been edited!"));
			plugin.getAssignmentManager().save();
		} catch (IllegalArgumentException e) {
			source.sendMessage(Text.of(TextColors.RED, "That is not a valid assignment type."));
		} catch (IndexOutOfBoundsException e) {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
	}

}
