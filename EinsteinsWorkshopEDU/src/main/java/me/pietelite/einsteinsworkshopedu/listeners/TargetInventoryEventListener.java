package me.pietelite.einsteinsworkshopedu.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.item.inventory.TargetInventoryEvent;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class TargetInventoryEventListener implements EventListener<TargetInventoryEvent> {

	private EWEDUPlugin plugin;
	
	public TargetInventoryEventListener(EWEDUPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(TargetInventoryEvent event) throws Exception {
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
