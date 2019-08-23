package me.pietelite.einsteinsworkshopedu.listeners;

import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.item.inventory.TargetInventoryEvent;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class TargetInventoryEventListener implements EventListener<TargetInventoryEvent> {

	private EweduPlugin plugin;
	
	public TargetInventoryEventListener(EweduPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(TargetInventoryEvent event) {
		Object root = event.getCause().root();
		if (root instanceof Player) {
			Player movingPlayer = (Player) root;
			if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager()).getFrozenPlayers().contains(movingPlayer.getUniqueId())) {
				try {
					((Cancellable) event).setCancelled(true);
				} catch (ClassCastException e) {
					// ignore
				}
			}
		}
	}
	
}
