package me.pietelite.einsteinsworkshopedu.features.boxes;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLocationManager {

    final private EWEDUPlugin plugin;

    private HashMap<UUID, SimpleLocation> playerLocations = new HashMap<>();

    public PlayerLocationManager(EWEDUPlugin plugin) {
        this.plugin = plugin;
    }

    public void putPlayerLocation(UUID uuid, SimpleLocation simpleLocation) {
        playerLocations.put(uuid, simpleLocation);
    }

    public SimpleLocation getPlayerLocation(UUID uuid) {
        return playerLocations.get(uuid);
    }

}
