package me.pietelite.einsteinsworkshopedu.listeners;

import me.pietelite.einsteinsworkshopedu.features.boxes.Box;
import me.pietelite.einsteinsworkshopedu.features.boxes.BoxManager;
import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.action.InteractEvent;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.NoSuchElementException;

public class InteractEventListener implements EventListener<InteractEvent> {

	private EweduPlugin plugin;
	
	public InteractEventListener(EweduPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void handle(InteractEvent event) {
		Object root = event.getCause().root();
		if (root instanceof Player) {
			Player player = (Player) root;
			if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager()).getFrozenPlayers().contains(player.getUniqueId())) {
				try {
					event.setCancelled(true);
					return;
				} catch (ClassCastException e) {
					// ignore
					return;
				}
			}

			for (Box box : ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getElements()) {
				try {
					if (!player.hasPermission("einsteinsworkshop.instructor") &&
							box.contains(new SimpleLocation(
								event.getInteractionPoint().get().getFloorX(),
								event.getInteractionPoint().get().getFloorY(),
								event.getInteractionPoint().get().getFloorZ(),
								player.getLocation().getExtent())) && !box.building) {
						player.sendMessage(Text.of(TextColors.RED, "You can't edit here!"));
						event.setCancelled(true);
						break;
					}
				} catch (ClassCastException | NoSuchElementException e) {
					// ignore
					return;
				}
			}
			if (event instanceof InteractBlockEvent.Primary) {
				InteractBlockEvent.Primary primaryInteractEvent = (InteractBlockEvent.Primary) event;
				if (player.getItemInHand(HandTypes.MAIN_HAND).get().getType().getName().equals(
						((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getWandItemName())) {
					SimpleLocation newLocation = new SimpleLocation(primaryInteractEvent.getTargetBlock().getPosition(),player.getWorld());
					SimpleLocation priorLocation = ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getPosition1Map().put(
							player.getUniqueId(),
							newLocation
					);
					if (!newLocation.equals(priorLocation)) {
						player.sendMessage(Text.of(TextColors.GREEN, "Position 1 set!"));
					}
					event.setCancelled(true);
				}
			}
			if (event instanceof InteractBlockEvent.Secondary) {
				InteractBlockEvent.Secondary secondaryInteractEvent = (InteractBlockEvent.Secondary) event;
				if (player.getItemInHand(HandTypes.MAIN_HAND).get().getType().getName().equals(
						((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getWandItemName())) {
					SimpleLocation newLocation = new SimpleLocation(secondaryInteractEvent.getTargetBlock().getPosition(),player.getWorld());
					SimpleLocation priorLocation = ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager()).getPosition2Map().put(
							player.getUniqueId(),
							newLocation
					);
					if (!newLocation.equals(priorLocation)) {
						player.sendMessage(Text.of(TextColors.GREEN, "Position 2 set!"));
					}
					event.setCancelled(true);
				}
			}
		}
	}
}
