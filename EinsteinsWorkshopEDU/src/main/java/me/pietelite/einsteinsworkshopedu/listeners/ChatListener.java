package me.pietelite.einsteinsworkshopedu.listeners;

import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class ChatListener implements EventListener<MessageChannelEvent.Chat> {

	private EweduPlugin plugin;
	
	public ChatListener(EweduPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handle(Chat event) throws Exception {
		Object root = event.getCause().root();
		if (root instanceof Player) {
			Player player = (Player) root;
			if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager()).getFrozenPlayers().contains(player)) {
				player.sendMessage(Text.of(TextColors.RED, "You cannot chat while frozen!"));
				event.setCancelled(true);
			}
		}
	}

	public void sendModifiedMessage(Player player, Text text) {
		Sponge.getServer().getBroadcastChannel().send(Text.of("<User> ", text));
	}

}
