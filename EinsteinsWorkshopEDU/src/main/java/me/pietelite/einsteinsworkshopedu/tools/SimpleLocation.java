package me.pietelite.einsteinsworkshopedu.tools;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SimpleLocation {

    final private int blockX;
    final private int blockY;
    final private int blockZ;
    final private World world;

    public SimpleLocation(int blockX, int blockY, int blockZ, World world) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.world = world;
    }

    public SimpleLocation(Vector3d vector, World world) {
        this(vector.getFloorX(), vector.getFloorY(), vector.getFloorZ(), world);
    }

    public SimpleLocation(Location<World> location) {
        if (location == null) {
            this.blockX = 0;
            this.blockY = 0;
            this.blockZ = 0;
            this.world = null;
        } else {
            this.blockX = location.getBlockX();
            this.blockY = location.getBlockY();
            this.blockZ = location.getBlockZ();
            this.world = location.getExtent();
        }
    }

    public SimpleLocation(Vector3i position, World world) {
        this(position.getX(), position.getY(), position.getZ(), world);
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public Vector3d getPosition() {
        return new Vector3d(getBlockX(), getBlockY(), getBlockZ());
    }

    public World getWorld() {
        return world;
    }

    public boolean equals(SimpleLocation other) {
        if (other == null) {
            return false;
        }
        return this.blockX == other.blockX && this.blockY == other.blockY && this.blockZ == other.blockZ;
    }
}
