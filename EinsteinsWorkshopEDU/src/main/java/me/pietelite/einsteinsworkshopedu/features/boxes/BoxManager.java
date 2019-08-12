package me.pietelite.einsteinsworkshopedu.features.boxes;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BoxManager {

    private List<Box> boxes = new LinkedList<Box>();
    final private EWEDUPlugin plugin;

    HashMap<UUID, Location<World>> position1Map;
    HashMap<UUID, Location<World>> position2Map;

    public BoxManager(EWEDUPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

}
