package me.pietelite.einsteinsworkshopedu.listeners;

import java.util.NoSuchElementException;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class LoginEventListener implements EventListener<ClientConnectionEvent.Join> {

	private EWEDUPlugin plugin;
	
	public LoginEventListener(EWEDUPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(ClientConnectionEvent.Join event) throws Exception {
		Player player;
		try {
			player = event.getTargetEntity();
		} catch (NoSuchElementException e) {
			plugin.getLogger().warn("A playing logging in could not be accessed.");
			return;
		}
		player.sendMessage(Text.builder("Welcome to the ").color(TextColors.LIGHT_PURPLE)
				.append(Text.builder("Einstein's Workshop Server").color(TextColors.GOLD).style(TextStyles.BOLD).build())
				.append(Text.of(TextColors.LIGHT_PURPLE, "!")).build());
		player.sendMessage(Text.of(TextColors.LIGHT_PURPLE, "Please listen to your instructors."));
		for (String line : plugin.getLoginMessage()) {
			player.sendMessage(Text.of(line));
		}
		
		if (plugin.getFreezeManager().isAllFrozen 
				&& !plugin.getFreezeManager().getFrozenPlayers().contains(player)
				&& !player.hasPermission("einsteinsworkshop.freezeimmunity")) {
			plugin.getFreezeManager().getFrozenPlayers().add(player);
			player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
		}
	}

}
