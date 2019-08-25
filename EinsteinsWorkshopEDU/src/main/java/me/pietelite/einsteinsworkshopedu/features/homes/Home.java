package me.pietelite.einsteinsworkshopedu.features.homes;

import com.flowpowered.math.vector.Vector3d;
import java.util.UUID;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.StorageLine;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

public class Home implements EweduElement {

  private final UUID playerUuid;
  private final Transform<World> transform;

  Home(UUID playerUuid, Transform<World> transform) {
    this.playerUuid = playerUuid;
    this.transform = transform;
  }

  Home(UUID playerUuid, World world, Vector3d translation, Vector3d rotation) {
    this.transform = new Transform<>(world, translation, rotation);
    this.playerUuid = playerUuid;
  }

  Home(UUID playerUuid,
       double xlocation, double ylocation, double zlocation,
       double rotation1, double rotation2, double rotation3, World world) {
    this(
        playerUuid,
        world,
        new Vector3d(xlocation, ylocation, zlocation),
        new Vector3d(rotation1, rotation2, rotation3)
    );
  }

  @Override
  public StorageLine toStorageLine() {
    return StorageLine.builder()
        .addItem(playerUuid.toString())
        .addItem(transform.getPosition().getX())
        .addItem(transform.getPosition().getY())
        .addItem(transform.getPosition().getZ())
        .addItem(transform.getRotation().getX())
        .addItem(transform.getRotation().getY())
        .addItem(transform.getRotation().getZ())
        .addItem(transform.getExtent().getUniqueId().toString())
        .build();
  }

  @Override
  public Text formatReadable(int id) {
    return Text.of(getTransform().toString());
  }

  UUID getPlayerUuid() {
    return playerUuid;
  }

  private Transform<World> getTransform() {
    return transform;
  }

  boolean teleport(Player player) {
    return player.setTransformSafely(this.transform);
  }
}
