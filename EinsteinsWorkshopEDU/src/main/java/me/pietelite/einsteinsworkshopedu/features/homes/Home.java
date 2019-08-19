package me.pietelite.einsteinsworkshopedu.features.homes;

import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.StorageLine;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class Home implements EweduElement {

    private final UUID playerUUID;
    private final Transform<World> transform;

    public Home(UUID playerUUID, Transform<World> transform) {
        this.playerUUID = playerUUID;
        this.transform = transform;
    }

    public Home(UUID playerUUID, World world, Vector3d translation, Vector3d rotation) {
        this.transform = new Transform(world, translation, rotation);
        this.playerUUID = playerUUID;
    }

    public Home(UUID playerUUID, double xLocation, double yLocation, double zLocation, double rotation1, double rotation2, double rotation3, World world) {
        this(
                playerUUID,
                world,
                new Vector3d(xLocation, yLocation, zLocation),
                new Vector3d(rotation1, rotation2, rotation3)
        );
    }

    @Override
    public StorageLine toStorageLine() {
        return StorageLine.builder()
                .addItem(playerUUID.toString())
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

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Transform<World> getTransform() {
        return transform;
    }

    public boolean teleport(Player player) {
        return player.setTransformSafely(this.transform);
    }
}
