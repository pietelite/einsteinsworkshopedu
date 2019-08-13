package me.pietelite.einsteinsworkshopedu.features.boxes;

import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.World;

import java.util.*;

public class Box {

    final private Vector3d position1;
    final private Vector3d position2;
    final private UUID worldUUID;
    public boolean movement = false;
    public boolean building = false;
    private static final float LOCATION_CONDITION_OFFSET = 0.01f;

    public enum Position {
        POSITION1,
        POSITION2
    }

    Box(Vector3d position1, Vector3d position2, UUID worldUUID) {
        this.position1 = position1;
        this.position2 = position2;
        this.worldUUID = worldUUID;
    }

    Box(Vector3d position1, Vector3d position2, World world) {
        this(position1, position2, world.getUniqueId());
    }

    Box(Vector3d position1, Vector3d position2, UUID worldUUID, boolean building, boolean movement) {
        this(position1, position2, worldUUID);
        this.building = building;
        this.movement = movement;
    }

    Box(Vector3d position1, Vector3d position2, World world, boolean building, boolean movement) {
        this(position1, position2, world.getUniqueId(), building, movement);
    }

    public boolean contains(SimpleLocation simpleLocation) {
        return  getXMin() - LOCATION_CONDITION_OFFSET <= simpleLocation.getBlockX() &&
                getXMax() + LOCATION_CONDITION_OFFSET >= simpleLocation.getBlockX() &&
                getYMin() - LOCATION_CONDITION_OFFSET <= simpleLocation.getBlockY() &&
                getYMax() + LOCATION_CONDITION_OFFSET >= simpleLocation.getBlockY() &&
                getZMin() - LOCATION_CONDITION_OFFSET <= simpleLocation.getBlockZ() &&
                getZMax() + LOCATION_CONDITION_OFFSET>= simpleLocation.getBlockZ() &&
                getWorld().equals(simpleLocation.getWorld());
    }

    public boolean contains(Player player) {
        return contains(new SimpleLocation(player.getTransform().getPosition(), player.getWorld()));
    }

    public boolean isOverlapping(Box other) {
        if (!this.getWorld().equals(other.getWorld())) {
            return false;
        }
        for (Vector3d vector : getCorners()) {
            if (other.contains(new SimpleLocation(vector.getFloorX(), vector.getFloorY(), vector.getFloorZ(), this.getWorld()))) {
                return true;
            }
        }
        return false;
    }

    private List<Vector3d> getCorners() {
        List<Vector3d> output = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Vector3d corner = new Vector3d(
                    ((i / 4) % 2 == 0) ? position1.getFloorX() : position2.getFloorX(),
                    ((i / 2) % 2 == 0) ? position1.getFloorY() : position2.getFloorY(),
                    ((i    ) % 2 == 0) ? position1.getFloorZ() : position2.getFloorZ());
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

    Text formatReadable(int id) {
        return Text.of(TextColors.GRAY, "Box " + id + ".");
    }

    public Text formatReadableVerbose(int id) {
        return Text.builder("Box " + id).color(TextColors.RED)
                .append(Text.of(TextColors.GRAY, ": "))
                .append(Text.of(TextColors.YELLOW, "\nMovement: ", TextColors.AQUA, this.movement))
                .append(Text.of(TextColors.YELLOW, "\nBuilding: ", TextColors.AQUA, this.building))
                .build();
    }

    public String formatStorage() {
        return String.join(";",
                this.getWorld().getUniqueId().toString(),
                String.valueOf(this.position1.getX()),
                String.valueOf(this.position1.getY()),
                String.valueOf(this.position1.getZ()),
                String.valueOf(this.position2.getX()),
                String.valueOf(this.position2.getY()),
                String.valueOf(this.position2.getZ()),
                String.valueOf(this.movement),
                String.valueOf(this.building));
    }

    public int getXMin() {
        return Math.min(this.position1.getFloorX(), this.position2.getFloorX());
    }

    private int getXMax() {
        return Math.max(this.position1.getFloorX(), this.position2.getFloorX());
    }

    public int getYMin() {
        return Math.min(this.position1.getFloorY(), this.position2.getFloorY());
    }

    private int getYMax() {
        return Math.max(this.position1.getFloorY(), this.position2.getFloorY());
    }

    public int getZMin() {
        return Math.min(this.position1.getFloorZ(), this.position2.getFloorZ());
    }

    private int getZMax() {
        return Math.max(this.position1.getFloorZ(), this.position2.getFloorZ());
    }

    public World getWorld() {
        try {
            return Sponge.getServer().getWorld(worldUUID).get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

}
