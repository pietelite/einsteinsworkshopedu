package me.pietelite.einsteinsworkshopedu.freeze;

import javax.annotation.Syntax;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("freeze|f")
@Syntax("/ew freeze all|player")
@CommandPermission("einsteinsworkshop.command.freeze")
public class FreezeCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public FreezeCommand(EWEDUPlugin plugin) {
    	super(plugin);
    }
    
    @Subcommand("all|a")
    public void onFreezeAll(CommandSource source) {
    	Player sender = (Player) source;
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("einsteinsworkshop.freezeimmunity")) {
				plugin.getFreezeManager().freeze(player);
				sender.sendMessage(Text.of(TextColors.GOLD, "Froze ")
		    			.concat(Text.of(TextColors.LIGHT_PURPLE, "all players"))
		    			.concat(Text.of(TextColors.GOLD, ".")));
				player.sendMessage(Text.of(TextColors.RED, "You have been frozen!"));
			}
		}
    }
    
    @Subcommand("player|p")
    @CommandCompletion("players")
    public void onFreezePlayer(CommandSource source, String name) {
    	Player sender = (Player) source;
		for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name) &&
					!onlinePlayer.hasPermission("einsteinsworkshop.freezeimmunity")) {
				plugin.getFreezeManager().freeze(onlinePlayer);
				sender.sendMessage(Text.of(TextColors.GOLD, "Froze ")
		    			.concat(Text.of(TextColors.LIGHT_PURPLE, name))
		    			.concat(Text.of(TextColors.GRAY, " (" + onlinePlayer.getName() + ")"))
		    			.concat(Text.of(TextColors.GOLD, ".")));
				onlinePlayer.sendMessage(Text.of(TextColors.RED, "You have been frozen!"));
				return;
			}
		}
		sender.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
}
