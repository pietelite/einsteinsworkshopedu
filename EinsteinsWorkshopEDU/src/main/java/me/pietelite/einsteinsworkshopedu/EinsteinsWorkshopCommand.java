package me.pietelite.einsteinsworkshopedu;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@CommandAlias("einsteinsworkshop|ew")
public class EinsteinsWorkshopCommand extends BaseCommand {

	/** The main plugin object. */
    protected final EweduPlugin plugin;
    protected static List<SubCommand> eweduSubCommands = new LinkedList<>();

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public EinsteinsWorkshopCommand(EweduPlugin plugin) {
        this.plugin = plugin;
    }

    protected static boolean addSubCommand(SubCommand subCommand) {
    	return eweduSubCommands.add(subCommand);
	}

    @Default
    @HelpCommand
    public void onHelp(CommandSource source, CommandHelp help){
    	Player player = (Player) source;
    	player.sendMessage(Text.of(TextColors.GOLD, "Help: /einsteinsworkshop (/ew)"));
    	player.sendMessage(Text.of(TextColors.GOLD, "-------------"));

    	for (SubCommand subCommand : eweduSubCommands) {
    		if (player.hasPermission(subCommand.getPermission())) {
    			player.sendMessage(Text.of(TextColors.AQUA, subCommand.getSyntax()));
			}
		}

//    	if (player.hasPermission("einsteinsworkshop.instructor")) {
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew freeze all|player"));
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew unfreeze all|player"));
//    	}
//
//    	if (player.hasPermission("einsteinsworkshop.instructor"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment add <type> <body...>"));
//
//    	if (player.hasPermission("einsteinsworkshop.instructor"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment remove <id>"));
//
//		if (player.hasPermission("einsteinsworkshop.student"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment list"));
//
//    	if (player.hasPermission("einsteinsworkshop.instructor"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment remove <id>"));
//
//		if (player.hasPermission("einsteinsworkshop.student"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment complete <id>"));
//
//    	if (player.hasPermission("einsteinsworkshop.instructor"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment edit <title|body|type> <id> <value>"));
//
//    	if (player.hasPermission("einsteinsworkshop.student"))
//    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment info <id>"));
    }

    protected static class SubCommand {
    	private final String permission;
    	private final String syntax;

    	public SubCommand(String permission, String syntax) {
    		this.permission = permission;
    		this.syntax = syntax;
		}

		public String getPermission() {
			return permission;
		}

		public String getSyntax() {
    		return syntax;
		}
	}

}
