package me.pietelite.einsteinsworkshopedu.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.action.InteractEvent;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class InteractEventListener implements EventListener<InteractEvent> {

	private EWEDUPlugin plugin;
	
	public InteractEventListener(EWEDUPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(InteractEvent event) throws Exception {
		Object root = event.getCause().root();
		if (root instanceof Player) {
			Player movingPlayer = (Player) root;
			if (plugin.getFreezeManager().getFrozenPlayers().contains(movingPlayer)) {
				try {
					((Cancellable) event).setCancelled(true);
					return;
				} catch (ClassCastException e) {
					// ignore
					return;
				}
			}
		}
	}

}
