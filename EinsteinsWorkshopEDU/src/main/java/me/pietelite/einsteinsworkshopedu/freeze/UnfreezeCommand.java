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
@Subcommand("unfreeze|uf")
@Syntax("/ew unfreeze all|player")
@CommandPermission("einsteinsworkshop.command.unfreeze")
public class UnfreezeCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public UnfreezeCommand(EWEDUPlugin plugin) {
    	super(plugin);
    }
    
    @Subcommand("all|a")
    public void onFreezeAll(CommandSource source) {
    	Player sender = (Player) source;
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			if (plugin.getFreezeManager().unfreeze(player)) {
				player.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
			}
		}
		sender.sendMessage(Utils.createFreezeAllMessage(false));
    }
    
    @Subcommand("player|p")
    @CommandCompletion("@players")
    public void onFreezePlayer(CommandSource source, String name) {
    	Player sender = (Player) source;
		for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
				if (plugin.getFreezeManager().unfreeze(onlinePlayer)) {
					sender.sendMessage(Utils.createFreezePlayerMessage(false, name));
					onlinePlayer.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
					return;
				} else {
					sender.sendMessage(Text.of(TextColors.RED, "That player wasn't frozen!"));
				}
			}
		}
		sender.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
    
}
