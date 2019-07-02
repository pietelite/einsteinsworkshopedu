package me.pietelite.einsteinsworkshopedu.listeners;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.advancement.AdvancementEvent;
import org.spongepowered.api.event.entity.TargetEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.ChangeGameModeEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class TargetEntityEventListener implements EventListener<TargetEntityEvent> {
	
	private EWEDUPlugin plugin;
	
	public TargetEntityEventListener(EWEDUPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(TargetEntityEvent event) throws Exception {
		
		if ((event instanceof ChangeGameModeEvent) ||
				(event instanceof AdvancementEvent) ||
				(event instanceof KickPlayerEvent)) {
			return;
		}
		
		Entity movingEntity = event.getTargetEntity();
		if (movingEntity instanceof Player) {
			Player movingPlayer = (Player) movingEntity;
			if (plugin.getFreezeManager().isFrozen(movingPlayer)) {
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
