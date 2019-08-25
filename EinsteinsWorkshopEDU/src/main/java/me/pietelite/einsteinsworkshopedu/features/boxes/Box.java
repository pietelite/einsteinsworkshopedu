package me.pietelite.einsteinsworkshopedu.features.boxes;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.StorageLine;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Box implements EweduElement {

  private final Vector3i position1;
  private final Vector3i position2;
  private final UUID worldUuid;
  public boolean movement = false;
  public boolean building = false;
  private static final float LOCATION_CONDITION_OFFSET = 0.01f;

  @Override
  public StorageLine toStorageLine() {
    return StorageLine.builder()
        .addItem(Objects.requireNonNull(this.getWorld()).getUniqueId().toString())
        .addItem(this.position1.getX())
        .addItem(this.position1.getY())
        .addItem(this.position1.getZ())
        .addItem(this.position2.getX())
        .addItem(this.position2.getY())
        .addItem(this.position2.getZ())
        .addItem(this.movement)
        .addItem(this.building)
        .build();
  }

  public enum Position {
    POSITION1,
    POSITION2
  }

  private Box(Vector3i position1, Vector3i position2, UUID worldUuid) {
    this.position1 = position1;
    this.position2 = position2;
    this.worldUuid = worldUuid;
  }

  Box(Vector3i position1, Vector3i position2, World world) {
    this(position1, position2, world.getUniqueId());
  }

  Box(Vector3i position1, Vector3i position2, UUID worldUuid, boolean building, boolean movement) {
    this(position1, position2, worldUuid);
    this.building = building;
    this.movement = movement;
  }

  /**
   * Determines whether the given location is inside this box.
   *
   * @param location The queried Location, with Extent type 'World'
   * @return true if the location is inside the box
   */
  public boolean contains(Location<World> location) {
    return getXMin() - LOCATION_CONDITION_OFFSET <= location.getBlockX()
        && getXMax() + LOCATION_CONDITION_OFFSET >= location.getBlockX()
        && getYMin() - LOCATION_CONDITION_OFFSET <= location.getBlockY()
        && getYMax() + LOCATION_CONDITION_OFFSET >= location.getBlockY()
        && getZMin() - LOCATION_CONDITION_OFFSET <= location.getBlockZ()
        && getZMax() + LOCATION_CONDITION_OFFSET >= location.getBlockZ()
        && Objects.equals(getWorld(), location.getExtent());
  }

  public boolean contains(Player player) {
    return contains(new Location<>(player.getWorld(), player.getTransform().getLocation().getPosition()));
  }

  boolean isOverlapping(Box other) {
    if (!Objects.equals(this.getWorld(), other.getWorld())) {
      return false;
    }
    for (Vector3d vector : getCorners()) {
      if (other.contains(new Location<>(this.getWorld(), vector.getFloorX(), vector.getFloorY(), vector.getFloorZ()))) {
        return true;
      }
    }
    return false;
  }

  private List<Vector3d> getCorners() {
    List<Vector3d> output = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      Vector3d corner = new Vector3d(
          ((i / 4) % 2 == 0) ? position1.getX() : position2.getX(),
          ((i / 2) % 2 == 0) ? position1.getY() : position2.getY(),
          ((i) % 2 == 0) ? position1.getZ() : position2.getZ());
      output.add(corner);
    }
    return output;
  }

  public void spawnBorderParticles(Player player, Color color) {
    for (int y = getYMin(); y <= getYMax() + 1; y++) {
      for (int z = getZMin(); z <= getZMax() + 1; z++) {
        spawnBorderParticle(player, color, new Vector3d(getXMin(), y, z));
        spawnBorderParticle(player, color, new Vector3d(getXMax() + 1, y, z));
      }
    }
    for (int x = getXMin(); x <= getXMax() + 1; x++) {
      for (int z = getZMin(); z <= getZMax() + 1; z++) {
        spawnBorderParticle(player, color, new Vector3d(x, getYMin(), z));
        spawnBorderParticle(player, color, new Vector3d(x, getYMax() + 1, z));
      }
    }
    for (int x = getXMin(); x <= getXMax() + 1; x++) {
      for (int y = getYMin(); y <= getYMax() + 1; y++) {
        spawnBorderParticle(player, color, new Vector3d(x, y, getZMin()));
        spawnBorderParticle(player, color, new Vector3d(x, y, getZMax() + 1));
      }
    }
  }

  private void spawnBorderParticle(Player player, Color color, Vector3d vector) {
    player.spawnParticles(
        ParticleEffect.builder()
            .type(ParticleTypes.BARRIER)
            .quantity(1)
            .option(ParticleOptions.COLOR, color)
            .option(ParticleOptions.SCALE, 0.5)
            .build(),
        vector);
  }

  public Text formatReadable(int id) {
    return Text.of(TextColors.GRAY, "Box " + id + ".");
  }

  public Text formatReadableVerbose(int id) {
    return Text.builder("Box " + id).color(TextColors.RED)
        .append(Text.of(TextColors.GRAY, ": "))
        .append(Text.of(TextColors.YELLOW, "\nMovement: ", TextColors.AQUA, this.movement))
        .append(Text.of(TextColors.YELLOW, "\nBuilding: ", TextColors.AQUA, this.building))
        .build();
  }

  int getXMin() {
    return Math.min(this.position1.getX(), this.position2.getX());
  }

  private int getXMax() {
    return Math.max(this.position1.getX(), this.position2.getX());
  }

  int getYMin() {
    return Math.min(this.position1.getY(), this.position2.getY());
  }

  private int getYMax() {
    return Math.max(this.position1.getY(), this.position2.getY());
  }

  int getZMin() {
    return Math.min(this.position1.getZ(), this.position2.getZ());
  }

  private int getZMax() {
    return Math.max(this.position1.getZ(), this.position2.getZ());
  }

  private World getWorld() {
    if (Sponge.getServer().getWorld(worldUuid).isPresent()) {
      return Sponge.getServer().getWorld(worldUuid).get();
    } else {
      throw new NoSuchElementException();
    }
  }

}
