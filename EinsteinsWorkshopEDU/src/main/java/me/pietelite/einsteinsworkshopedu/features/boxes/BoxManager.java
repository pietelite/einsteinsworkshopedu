package me.pietelite.einsteinsworkshopedu.features.boxes;

import com.flowpowered.math.vector.Vector3i;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.EweduElementManager;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BoxManager extends EweduElementManager<Box> {

  private static final String BOXES_FILE_NAME = "boxes.txt";
  private static final String DEFAULT_BOXES_ASSET_FILE = "default_boxes.txt";

  private ItemType wandItem = ItemTypes.BRICK;

  public static final Box NONE = new Box(
      null,
      null,
      UUID.randomUUID(),
      true,
      true);

  private HashMap<UUID, Location<World>> position1Map = new HashMap<>();
  private HashMap<UUID, Location<World>> position2Map = new HashMap<>();

  /**
   * Primary constructor for a BoxManager.
   * @param plugin The functional instance of this plugin class
   */
  public BoxManager(EweduPlugin plugin) {
    super(
        plugin,
        line -> {
          String[] tokens = line.getTokens();
          if (tokens.length != 9) {
            throw new IllegalArgumentException();
          }
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
          } catch (NumberFormatException numException) {
            plugin.getLogger().error("The line could not be formed into a Box object because of a "
                + "NumberFormatException: " + String.join(",", tokens));
            return null;
          } catch (NoSuchElementException elemException) {
            plugin.getLogger().error("The line could not be formed into a Box object because of a "
                + "NoSuchElementException: " + String.join(",", tokens));
            return null;
          }
        },
        BOXES_FILE_NAME,
        DEFAULT_BOXES_ASSET_FILE
    );
  }

  boolean hasSelection(UUID uuid) {
    return position1Map.containsKey(uuid) && position2Map.containsKey(uuid);
  }

  public HashMap<UUID, Location<World>> getPosition1Map() {
    return position1Map;
  }

  public HashMap<UUID, Location<World>> getPosition2Map() {
    return position2Map;
  }

  public ItemType getWandItem() {
    return wandItem;
  }
}
