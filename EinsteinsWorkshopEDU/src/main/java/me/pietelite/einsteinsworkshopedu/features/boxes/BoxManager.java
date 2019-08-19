package me.pietelite.einsteinsworkshopedu.features.boxes;

import com.flowpowered.math.vector.Vector3i;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.EweduElementManager;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BoxManager extends EweduElementManager<Box> {

    private static final String BOXES_FILE_NAME = "boxes.txt";
    private static final String DEFAULT_BOXES_ASSET_FILE = "default_boxes.txt";

    private String wandItemName = ItemTypes.BRICK.getName();


    public static final Box NONE = new Box(
            null,
            null,
            UUID.randomUUID(),
            true,
            true);

    private HashMap<UUID, SimpleLocation> position1Map = new HashMap<>();
    private HashMap<UUID, SimpleLocation> position2Map = new HashMap<>();

    public BoxManager(EweduPlugin plugin) {
        super(
                plugin,
                EweduPlugin.FeatureTitle.BOXES,
                line -> {
                    String[] tokens = line.getTokens();
                    if (tokens.length != 9) throw new IllegalArgumentException();
                    try {
                        return new Box(
                                new Vector3i(
                                        Math.floor(Float.parseFloat(tokens[1])),
                                        Math.floor(Float.parseFloat(tokens[2])),
                                                Math.floor(Float.parseFloat(tokens[3]))),
                                new Vector3i(
                                        Math.floor(Float.parseFloat(tokens[4])),
                                        Math.floor(Float.parseFloat(tokens[5])),
                                        Math.floor(Float.parseFloat(tokens[6]))),
                                UUID.fromString(tokens[0]),
                                Boolean.parseBoolean(tokens[7]),
                                Boolean.parseBoolean(tokens[8]));
                    } catch (NumberFormatException num_e) {
                        plugin.getLogger().error("The line could not be formed into a Box object because of a " +
                                "NumberFormatException: " + String.join(",", tokens));
                        return null;
                    } catch (NoSuchElementException elem_e) {
                        plugin.getLogger().error("The line could not be formed into a Box object because of a " +
                                "NoSuchElementException: " + String.join(",", tokens));
                        return null;
                    }
                },
                BOXES_FILE_NAME,
                DEFAULT_BOXES_ASSET_FILE
        );
    }

    public boolean hasSelection(UUID uuid) {
        return position1Map.containsKey(uuid) && position2Map.containsKey(uuid);
    }

    public HashMap<UUID, SimpleLocation> getPosition1Map() {
        return position1Map;
    }

    public HashMap<UUID, SimpleLocation> getPosition2Map() {
        return position2Map;
    }

    public String getWandItemName() {
        return this.wandItemName;
    }

    public void setWandItemName(String wandItemName) {
        this.wandItemName = wandItemName;
    }

    public ItemType getWandItem() {
        switch(wandItemName) {
            case "minecraft:brick":
                return ItemTypes.BRICK;
            default:
                return ItemTypes.BRICK;
        }
    }
}
