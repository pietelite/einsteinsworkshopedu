package me.pietelite.einsteinsworkshopedu;

import javax.annotation.Syntax;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;

@CommandAlias("einsteinsworkshop|ew")
@Syntax("/ew freeze|unfreeze")
@CommandPermission("einsteinsworkshop.command")
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
    	if (player.hasPermission("einsteinsworkshop.command")) {
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew freeze all|player"));
    		player.sendMessage(Text.of(TextColors.AQUA, "/ew unfreeze all|player"));
    	}
    }
}
