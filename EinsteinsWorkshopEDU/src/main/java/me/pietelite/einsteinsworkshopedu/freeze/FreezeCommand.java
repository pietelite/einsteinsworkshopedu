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
    	if (!plugin.isFreezeEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
    	for (Player player : Sponge.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("einsteinsworkshop.freezeimmunity")) {
				if (plugin.getFreezeManager().freeze(player)) {
					player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
				}
			}
		}
		source.sendMessage(Utils.createFreezeAllMessage(true));
    }
    
    @Subcommand("player|p")
    @CommandCompletion("@players")
    public void onFreezePlayer(CommandSource source, String name) {
    	if (!plugin.isFreezeEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
    	for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
				if (!onlinePlayer.hasPermission("einsteinsworkshop.freezeimmunity")) {
					if (plugin.getFreezeManager().freeze(onlinePlayer)) {
						source.sendMessage(Utils.createFreezePlayerMessage(true, name));
						onlinePlayer.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
					} else {
						source.sendMessage(Text.of(TextColors.RED, "That player is already frozen!"));
					}
				} else {
					source.sendMessage(Text.of(TextColors.RED, "This player cannot be frozen!"));
				}
				return;
				
			}
		}
		source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
}
