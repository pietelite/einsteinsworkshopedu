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

@CommandAlias("einsteinsworkshop|ew")
public class EinsteinsWorkshopCommand extends BaseCommand {

	/** The main plugin object. */
    protected final EWEDUPlugin plugin;

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public EinsteinsWorkshopCommand(EWEDUPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Default
    @HelpCommand
    public void onHelp(CommandSource source, CommandHelp help){
    	Player player = (Player) source;
    	player.sendMessage(Text.of(TextColors.GOLD, "Help: /einsteinsworkshop (/ew)"));
    	player.sendMessage(Text.of(TextColors.GOLD, "-------------"));
    	if (player.hasPermission("einsteinsworkshop.command.freeze")) {
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew freeze all|player"));
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew unfreeze all|player"));
    	}
    	if (player.hasPermission("einsteinsworkshop.command.assignment.add"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment add <type> <id> <body...>"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.remove"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment remove <id>"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.list"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment list"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.remove"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment remove <id>"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.complete"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment complete <id>"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.edit"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment edit <title|body|type> <id> <value>"));
    	if (player.hasPermission("einsteinsworkshop.command.assignment.info"))
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew assignment info <id>"));
    	
    }
}
