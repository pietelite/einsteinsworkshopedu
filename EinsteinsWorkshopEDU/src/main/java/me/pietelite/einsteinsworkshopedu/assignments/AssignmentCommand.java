package me.pietelite.einsteinsworkshopedu.assignments;

import javax.annotation.Syntax;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("assignment|a")
@Syntax("/ew assignment add|remove|list")
@CommandPermission("einsteinsworkshop.command.assignment")
public class AssignmentCommand extends EinsteinsWorkshopCommand {

	public AssignmentCommand(EWEDUPlugin plugin) {
		super(plugin);
	}
	
	@Default
	@Subcommand("list")
	@CommandPermission("list")
	public void onList(CommandSource source) {
		source.sendMessage(Text.of(TextColors.GOLD, "-- All Assignments --"));
		for (int i = 0; i < plugin.getAssignmentManager().size(); i++) {
			source.sendMessage(plugin.getAssignmentManager().get(i).readable(i));
		}
	}
	
	@Subcommand("add")
	@CommandPermission("add")
	public void onAdd(CommandSource source, String[] args) {
		// TODO Write add command
	}
	
	@Subcommand("remove")
	@CommandPermission("remove")
	public void onRemove(CommandSource source, int id) {
		// TODO Write remove command
	}

}
