package me.pietelite.einsteinsworkshopedu.assignments;

import java.util.Date;

import javax.annotation.Syntax;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.assignments.Assignment.TitleTooLongException;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("assignment|a")
@Syntax("/ew assignment add|remove|list")
@CommandPermission("einsteinsworkshop.command.assignment")
public class AssignmentCommand extends EinsteinsWorkshopCommand {

	public AssignmentCommand(EWEDUPlugin plugin) {
		super(plugin);
	}
	
	// TODO Save the assignments on server stopping
	
	@Default
	@Subcommand("list")
	@CommandPermission("einsteinsworkshop.command.assignment.list")
	public void onList(CommandSource source) {
		if (plugin.getAssignmentManager().isEmpty()) {
			source.sendMessage(Text.of(TextColors.YELLOW, "There are no assignments yet."));
		}
		source.sendMessage(Text.of(TextColors.GOLD, "-- All Assignments --"));
		for (int i = 0; i < plugin.getAssignmentManager().size(); i++) {
			source.sendMessage(plugin.getAssignmentManager().get(i).readable(i + 1));
		}
	}
	
	@Subcommand("add")
	@CommandPermission("einsteinsworkshop.command.assignment.add")
	public void onAdd(CommandSource source, String type, String[] title) {
		try {
			plugin.getAssignmentManager().add(new Assignment(type, String.join(" ", title)));
			source.sendMessage(Text.of(TextColors.GREEN, "Assignment Created! Make sure to add a body message."));
			for (Player player : Sponge.getServer().getOnlinePlayers()) {
				player.sendMessage(Text.of(TextColors.YELLOW, "A new assignment has been created!"));
				player.sendMessage(Text.of(TextColors.YELLOW, "Type '/ew a list' to view it."));
			}
		} catch (IllegalArgumentException e) {
			source.sendMessage(Text.of(TextColors.RED, "That is not a valid assignment type."));
		} catch (TitleTooLongException e) {
			source.sendMessage(Text.of(TextColors.RED, "Titles can only be ", 
											Text.of(TextColors.GOLD, Assignment.MAXIMUM_TITLE_LENGTH),
											" characters long."));
		}
	}
	
	@Subcommand("remove")
	@CommandPermission("einsteinsworkshop.command.assignment.remove")
	public void onRemove(CommandSource source, int id) {
		if (plugin.getAssignmentManager().remove(id - 1) != null) {
			source.sendMessage(Text.of(TextColors.GREEN, "Assignment number ",
					Text.of(TextColors.GOLD, String.valueOf(id)),
					" was removed."));
			source.sendMessage(Text.of(TextColors.GREEN, "Assignments Renumbered."));
		} else {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
		
	}
	
	@Subcommand("edit title")
	@CommandPermission("einsteinsworkshop.command.assignment.edit")
	public void onEditTitle(CommandSource source, int id, String[] title) {
		try {
			Assignment assignment = plugin.getAssignmentManager().get(id - 1);
			assignment.setTitle(String.join(" ", title));
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Title set!"));
			sendEditMessage();
		} catch (TitleTooLongException e) {
			source.sendMessage(Text.of(TextColors.RED, "Titles can only be ", 
					Text.of(TextColors.GOLD, Assignment.MAXIMUM_TITLE_LENGTH),
					" characters long."));
		} catch (IndexOutOfBoundsException e) {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
	}
	
	@Subcommand("edit body")
	@CommandPermission("einsteinsworkshop.command.assignment.edit")
	public void onEditBody(CommandSource source, int id, String[] body) {
		try {
			Assignment assignment = plugin.getAssignmentManager().get(id - 1);
			assignment.setBody(String.join(" ", body));
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Body set!"));
			sendEditMessage();
		} catch (BodyTooLongException e) {
			source.sendMessage(Text.of(TextColors.RED, "Bodies can only be ",
					Text.of(TextColors.GOLD, Assignment.MAXIMUM_BODY_LENGTH),
					" characters long."));
		} catch (IndexOutOfBoundsException e) {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
	}
	
	@Subcommand("edit type")
	@CommandPermission("einsteinsworkshop.command.assignment.edit")
	public void onEditType(CommandSource source, int id, String type) {
		try {
			Assignment assignment = plugin.getAssignmentManager().get(id - 1);
			assignment.setType(type);
			assignment.setTimestamp(new Date());
			source.sendMessage(Text.of(TextColors.GREEN, "Type set!"));
			sendEditMessage();
		} catch (IllegalArgumentException e) {
			source.sendMessage(Text.of(TextColors.RED, "That is not a valid assignment type."));
		} catch (IndexOutOfBoundsException e) {
			source.sendMessage(Text.of(TextColors.RED, "There is no assignment with that id."));
		}
	}

	private void sendEditMessage() {
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(Text.of(TextColors.YELLOW, "An assignment has been edited!"));
		}
	}

}
