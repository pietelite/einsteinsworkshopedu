package me.pietelite.einsteinsworkshopedu.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class ChatListener implements EventListener<MessageChannelEvent.Chat> {

	private EWEDUPlugin plugin;
	
	public ChatListener(EWEDUPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handle(Chat event) throws Exception {
		Object root = event.getCause().root();
		if (root instanceof Player) {
			Player player = (Player) root;
			if (plugin.getFreezeManager().getFrozenPlayers().contains(player)) {
				player.sendMessage(Text.of(TextColors.RED, "You cannot chat while frozen!"));
				event.setCancelled(true);
			}
		}
	}

}
