package me.pietelite.einsteinsworkshopedu.features.boxes;

import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLocationManager {

    private HashMap<UUID, SimpleLocation> playerLocations = new HashMap<>();

    public void putPlayerLocation(UUID uuid, SimpleLocation simpleLocation) {
        playerLocations.put(uuid, simpleLocation);
    }

    public SimpleLocation getPlayerLocation(UUID uuid) {
        return playerLocations.get(uuid);
    }

}
