package me.pietelite.einsteinsworkshopedu.listeners;

import me.pietelite.einsteinsworkshopedu.features.boxes.Box;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.NoSuchElementException;

public class ChangeBlockListener implements EventListener<ChangeBlockEvent> {

    private EWEDUPlugin plugin;

    public ChangeBlockListener(EWEDUPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(ChangeBlockEvent event) {
        Object root = event.getCause().root();
        if (root instanceof Player) {
            Player player = (Player) root;
            if (plugin.getFreezeManager().getFrozenPlayers().contains(player)) {
                try {
                    event.setCancelled(true);
                    return;
                } catch (ClassCastException e) {
                    // ignore
                    return;
                }
            }

            for (Box box : plugin.getBoxManager().getBoxes()) {
                if (!box.building) {
                    try {
                        Location<World> location = event.getTransactions().get(0).getOriginal().getLocation().get();
                        if (!player.hasPermission("einsteinsworkshop.instructor") &&
                                box.contains(new SimpleLocation(location.getPosition(), location.getExtent()))) {
                            player.sendMessage(Text.of(TextColors.RED, "You can't build here!"));
                            event.setCancelled(true);
                            return;
                        }
                    } catch (ClassCastException | NoSuchElementException e) {
                        // ignore
                        return;
                    }
                }
            }
        }
    }
}
