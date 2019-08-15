package me.pietelite.einsteinsworkshopedu.features.freeze;

import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("unfreeze|uf")
@Syntax("/ew unfreeze all|player")
@CommandPermission("einsteinsworkshop.instructor")
public class UnfreezeCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public UnfreezeCommand(EweduPlugin plugin) {
    	super(plugin);
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew unfreeze all|a"));
		addSubCommand(new SubCommand("einsteinsworkshop.instructor", "/ew unfreeze player|p <username>"));
    }
    
    @Subcommand("all|a")
    public void onFreezeAll(CommandSource source) {
    	if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.FREEZE).isEnabled) {
			source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
			return;
		}
    	for (Player player : Sponge.getServer().getOnlinePlayers()) {
			if (plugin.getFreezeManager().getFrozenPlayers().remove(player)) {
				player.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
			}
		}
    	plugin.getFreezeManager().isAllFrozen = false;
		source.sendMessage(Utils.createFreezeAllMessage(false));
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
				if (plugin.getFreezeManager().getFrozenPlayers().remove(onlinePlayer)) {
					source.sendMessage(Utils.createFreezePlayerMessage(false, name));
					onlinePlayer.sendMessage(Text.of(TextColors.GREEN, "You have been unfrozen!"));
					return;
				} else {
					source.sendMessage(Text.of(TextColors.RED, "That player wasn't frozen!"));
				}
			}
		}
		source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
    
}
