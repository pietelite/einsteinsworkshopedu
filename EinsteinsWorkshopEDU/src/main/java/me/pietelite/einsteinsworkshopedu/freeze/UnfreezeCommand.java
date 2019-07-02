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
@Subcommand("unfreeze")
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
    	sender.sendMessage(Text.of(TextColors.GOLD, "Unfreezing... all"));
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			plugin.getFreezeManager().freeze(player);
			player.sendMessage(Text.of(TextColors.RED, "You have been unfrozen!"));
		}
    }
    
    @Subcommand("player|p")
    @CommandCompletion("players")
    public void onFreezePlayer(CommandSource source, String name) {
    	Player sender = (Player) source;
    	sender.sendMessage(Text.of(TextColors.GOLD, "Unfreezing... " + name));
		for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
				plugin.getFreezeManager().unfreeze(onlinePlayer);
				onlinePlayer.sendMessage(Text.of(TextColors.RED, "You have been unfrozen!"));
			}
		}
    }
    
}
