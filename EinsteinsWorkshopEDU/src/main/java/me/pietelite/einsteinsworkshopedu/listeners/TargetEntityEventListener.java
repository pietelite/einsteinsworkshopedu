package me.pietelite.einsteinsworkshopedu.listeners;

import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.features.boxes.Box;
import me.pietelite.einsteinsworkshopedu.features.boxes.BoxManager;
import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.action.SleepingEvent;
import org.spongepowered.api.event.entity.*;
import org.spongepowered.api.event.entity.explosive.TargetExplosiveEvent;
import org.spongepowered.api.event.entity.item.TargetItemEvent;
import org.spongepowered.api.event.entity.living.TargetLivingEvent;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import org.spongepowered.api.event.entity.projectile.TargetProjectileEvent;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class TargetEntityEventListener implements EventListener<TargetEntityEvent> {
	
	private EweduPlugin plugin;

	public TargetEntityEventListener(EweduPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(@Nonnull TargetEntityEvent event) {

		if (event instanceof MoveEntityEvent.Teleport || (
				!(event instanceof AttackEntityEvent) &&
				!(event instanceof ChangeInventoryEvent) &&
				!(event instanceof DamageEntityEvent) &&
				!(event instanceof DestructEntityEvent) &&
				!(event instanceof HealEntityEvent) &&
				!(event instanceof IgniteEntityEvent) &&
				!(event instanceof InteractEntityEvent) &&
				!(event instanceof MoveEntityEvent) &&
				!(event instanceof RideEntityEvent) &&
				!(event instanceof SleepingEvent) &&
				!(event instanceof TameEntityEvent) &&
				!(event instanceof TargetExplosiveEvent) &&
				!(event instanceof TargetItemEvent) &&
				!(event instanceof TargetLivingEvent) &&
				!(event instanceof TargetProjectileEvent) &&
				!(event instanceof UnleashEntityEvent))) {
			return;
		}

		Entity movingEntity = event.getTargetEntity();
		if (movingEntity instanceof Player) {
			Player movingPlayer = (Player) movingEntity;
			if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager()).getFrozenPlayers().contains(movingPlayer.getUniqueId())) {
				try {
					((Cancellable) event).setCancelled(true);
					return;
				} catch (ClassCastException e) {
					// ignore
					return;
				}
			}
			SimpleLocation lastSavedLocation = plugin.getPlayerLocationManager()
					.getPlayerLocation(movingPlayer.getUniqueId());
			SimpleLocation currentLocation = new SimpleLocation(
					movingPlayer.getLocation().getBlockX(),
					movingPlayer.getLocation().getBlockY(),
					movingPlayer.getLocation().getBlockZ(),
					movingPlayer.getLocation().getExtent()
			);
			if (lastSavedLocation == null) {
				plugin.getPlayerLocationManager().putPlayerLocation(movingPlayer.getUniqueId(), currentLocation);
			} else {
				Box lastBox = BoxManager.NONE;
				Box currentBox = BoxManager.NONE;
				int currentBoxIndex = 0;
				List<Box> boxes = ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getElements();
				for (int i = 0; i < boxes.size(); i++) {
					if (boxes.get(i).contains(lastSavedLocation)) {
						lastBox = boxes.get(i);
					}
					if (boxes.get(i).contains(currentLocation)) {
						currentBox = boxes.get(i);
						currentBoxIndex = i;
					}
				}
				if (lastBox.movement != currentBox.movement &&
						!movingPlayer.hasPermission("einsteinsworkshop.immunity")) {
					movingPlayer.sendMessage(Text.of(TextColors.RED, "You can't move there!"));
					if (currentBox == BoxManager.NONE) {
						lastBox.spawnBorderParticles(movingPlayer, Color.RED);
					} else {
						currentBox.spawnBorderParticles(movingPlayer, Color.RED);
					}
					movingPlayer.setTransform(new Transform<>(
							lastSavedLocation.getWorld(),
							new Vector3d(
									lastSavedLocation.getBlockX() + 0.5,
									lastSavedLocation.getBlockY() + 0.5,
									lastSavedLocation.getBlockZ() + 0.5),
							movingPlayer.getTransform().getRotation()
					));
				} else {
					if (!lastBox.equals(currentBox) &&
							!currentBox.equals(BoxManager.NONE) &&
							movingPlayer.hasPermission("einsteinsworkshop.instructor")) {
						movingPlayer.sendMessage(currentBox.formatReadableVerbose(currentBoxIndex + 1));
						currentBox.spawnBorderParticles(movingPlayer, Color.GREEN);
					}
					plugin.getPlayerLocationManager().putPlayerLocation(movingPlayer.getUniqueId(), currentLocation);
				}
			}
		}
	}
}
