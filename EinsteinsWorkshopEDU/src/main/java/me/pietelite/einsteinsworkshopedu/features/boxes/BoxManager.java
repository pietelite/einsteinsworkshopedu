package me.pietelite.einsteinsworkshopedu.features.boxes;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BoxManager {

    private List<Box> boxes;
    final private EWEDUPlugin plugin;
    private String wandItemName;

    final private BoxDataHandler boxDataHandler;

    public static final Box NONE = new Box(
            null,
            null,
            UUID.randomUUID(),
            true,
            true);

    private HashMap<UUID, SimpleLocation> position1Map = new HashMap<>();
    private HashMap<UUID, SimpleLocation> position2Map = new HashMap<>();

    public BoxManager(EWEDUPlugin plugin) {
        this.plugin = plugin;
        this.boxDataHandler = new BoxDataHandler(plugin);
        this.boxes = boxDataHandler.readBoxesFile();
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public boolean hasSelection(UUID uuid) {
        return position1Map.containsKey(uuid) && position2Map.containsKey(uuid);
    }


    public BoxDataHandler getBoxDataHandler() {
        return boxDataHandler;
    }

    public HashMap<UUID, SimpleLocation> getPosition1Map() {
        return position1Map;
    }

    public HashMap<UUID, SimpleLocation> getPosition2Map() {
        return position2Map;
    }

    public void saveBoxes() {
        this.boxDataHandler.writeToFile(this.boxes);
    }

    public String getWandItemName() {
        return this.wandItemName;
    }

    public void setWandItemName(String wandItemName) {
        this.wandItemName = wandItemName;
    }
}
