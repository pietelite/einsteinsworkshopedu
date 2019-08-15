package me.pietelite.einsteinsworkshopedu.features.boxes;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLocationManager {

    final private EweduPlugin plugin;

    private HashMap<UUID, SimpleLocation> playerLocations = new HashMap<>();

    public PlayerLocationManager(EweduPlugin plugin) {
        this.plugin = plugin;
    }

    public void putPlayerLocation(UUID uuid, SimpleLocation simpleLocation) {
        playerLocations.put(uuid, simpleLocation);
    }

    public SimpleLocation getPlayerLocation(UUID uuid) {
        return playerLocations.get(uuid);
    }

}
