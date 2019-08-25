package me.pietelite.einsteinsworkshopedu.features.boxes;

import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

// TODO: Replace this class with a better, more general
//  player manager to be accessed by other features

public class PlayerLocationManager {

  private HashMap<UUID, Location<World>> playerLocations = new HashMap<>();

  public void putPlayerLocation(UUID uuid, Location<World> location) {
    playerLocations.put(uuid, location);
  }

  public Location<World> getPlayerLocation(UUID uuid) {
    return playerLocations.get(uuid);
  }

}
