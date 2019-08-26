package me.pietelite.einsteinsworkshopedu.listeners;

import java.util.NoSuchElementException;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.boxes.Box;
import me.pietelite.einsteinsworkshopedu.features.boxes.BoxManager;
import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.action.InteractEvent;

import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
      if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
          .getFrozenPlayers().contains(player.getUniqueId())) {
        try {
          event.setCancelled(true);
          return;
        } catch (ClassCastException e) {
          // ignore
          return;
        }
      }

      for (Box box : ((BoxManager) plugin.getFeatures()
          .get(EweduPlugin.FeatureTitle.BOXES).getManager())
          .getElements()) {
        try {
          if (!player.hasPermission("einsteinsworkshop.instructor")) {
            if (event.getContext().get(EventContextKeys.BLOCK_HIT).isPresent()
                && event.getContext().get(EventContextKeys.BLOCK_HIT).get()
                .getLocation().isPresent()) {
              if (box.contains(event.getContext().get(EventContextKeys.BLOCK_HIT).get()
                  .getLocation().get()) && !box.building) {
                player.sendMessage(Text.of(TextColors.RED, "You can't edit here!"));
                event.setCancelled(true);
                break;
              }
            } else {
              throw new NoSuchElementException();
            }
          }
        } catch (ClassCastException | NoSuchElementException e) {
          // ignore
          return;
        }
      }
      if (event instanceof InteractBlockEvent.Primary) {
        InteractBlockEvent.Primary primaryInteractEvent = (InteractBlockEvent.Primary) event;
        if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()
            && player.getItemInHand(HandTypes.MAIN_HAND).get().getType().getName().equals(
            ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
                .getWandItem().getName())) {
          if (primaryInteractEvent.getTargetBlock().getLocation().isPresent()) {
            Location<World> newLocation = primaryInteractEvent.getTargetBlock().getLocation().get();
            Location<World> priorLocation = ((BoxManager) plugin.getFeatures()
                .get(EweduPlugin.FeatureTitle.BOXES).getManager())
                .getPosition1Map().put(
                    player.getUniqueId(),
                    newLocation
                );
            if (priorLocation == null
                || !newLocation.getBlockPosition().equals(priorLocation.getBlockPosition())
                || !newLocation.getExtent().equals(priorLocation.getExtent())) {
              player.sendMessage(Text.of(TextColors.GREEN, "Position 1 set!"));
            }
          } else {
            player.sendMessage(Text.of(TextColors.RED, "This position could not be found."));
          }
          event.setCancelled(true);
        }
      }
      if (event instanceof InteractBlockEvent.Secondary) {
        InteractBlockEvent.Secondary secondaryInteractEvent = (InteractBlockEvent.Secondary) event;
        if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()
            && player.getItemInHand(HandTypes.MAIN_HAND).get().getType().getName().equals(
            ((BoxManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.BOXES).getManager())
                .getWandItem().getName())) {
          if (secondaryInteractEvent.getTargetBlock().getLocation().isPresent()) {
            Location<World> newLocation = secondaryInteractEvent.getTargetBlock().getLocation()
                .get();
            Location<World> priorLocation = ((BoxManager) plugin.getFeatures()
                .get(EweduPlugin.FeatureTitle.BOXES).getManager())
                .getPosition2Map().put(
                    player.getUniqueId(),
                    newLocation
                );
            if (priorLocation == null
                || !newLocation.getBlockPosition().equals(priorLocation.getBlockPosition())
                || !newLocation.getExtent().equals(priorLocation.getExtent())) {
              player.sendMessage(Text.of(TextColors.GREEN, "Position 2 set!"));
            }
          } else {
            player.sendMessage(Text.of(TextColors.RED, "This position could not be found."));
          }
          event.setCancelled(true);
        }
      }
    }
  }
}
