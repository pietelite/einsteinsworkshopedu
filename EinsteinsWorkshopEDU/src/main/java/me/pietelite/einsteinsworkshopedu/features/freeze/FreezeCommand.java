package me.pietelite.einsteinsworkshopedu.features.freeze;

import javax.annotation.Syntax;

import co.aikar.commands.annotation.*;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("freeze|f")
@Syntax("/ew freeze all|player")
@CommandPermission("einsteinsworkshop.instructor")
public class FreezeCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public FreezeCommand(EweduPlugin plugin) {
    	super(plugin);
    }

    @Default
	@Subcommand("help")
	public void onHelp(CommandSource source) {
		if (source.hasPermission("einsteinsworkshop.instructor")) {
			source.sendMessage(commandMessage("/ew freeze|f", "all|a", ""));
			source.sendMessage(commandMessage("/ew freeze|f", "player|p <player>", ""));
		}
	}

    @Subcommand("all|a")
    public void onFreezeAll(CommandSource source) {
    	if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.FREEZE).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
    	for (Player player : Sponge.getServer().getOnlinePlayers()) {
			if (!player.hasPermission("einsteinsworkshop.immunity")) {
				if (!plugin.getFreezeManager().getFrozenPlayers().contains(player)) {
					plugin.getFreezeManager().getFrozenPlayers().add(player);
					player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
				}
			}
		}
    	plugin.getFreezeManager().isAllFrozen = true;
		source.sendMessage(Utils.createFreezeAllMessage(true));
    }
    
    @Subcommand("player|p")
    @CommandCompletion("@players")
    public void onFreezePlayer(CommandSource source, String name) {
    	if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.FREEZE).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
    	for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
			if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
				if (!onlinePlayer.hasPermission("einsteinsworkshop.immunity")) {
					if (!plugin.getFreezeManager().getFrozenPlayers().contains(onlinePlayer)) {
						plugin.getFreezeManager().getFrozenPlayers().add(onlinePlayer);
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
