package me.pietelite.einsteinsworkshopedu.listeners;

import java.util.NoSuchElementException;

import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class LoginEventListener implements EventListener<ClientConnectionEvent.Join> {

	private EweduPlugin plugin;
	
	public LoginEventListener(EweduPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(ClientConnectionEvent.Join event) {
		Player player;
		try {
			player = event.getTargetEntity();
		} catch (NoSuchElementException e) {
			plugin.getLogger().error("A player logging in could not be accessed.");
			return;
		}
		player.sendMessage(Text.builder("Welcome to the ").color(TextColors.LIGHT_PURPLE)
				.append(Text.builder("Einstein's Workshop Server").color(TextColors.GOLD).style(TextStyles.BOLD).build())
				.append(Text.of(TextColors.LIGHT_PURPLE, "!")).build());
		player.sendMessage(Text.of(TextColors.LIGHT_PURPLE, "Please listen to your instructors."));
		for (String line : plugin.getLoginMessage()) {
			player.sendMessage(Text.of(line));
		}
		
		if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager()).getFrozenPlayers().contains(player.getUniqueId())) {
			player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
		}
	}

}
