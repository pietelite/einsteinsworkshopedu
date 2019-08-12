package me.pietelite.einsteinsworkshopedu.features.boxes;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Box {

    private Location<World> location1;
    private Location<World> location2;

    Box(Location<World> location1, Location<World> location2) {
        this.location1 = location1;
        this.location2 = location2;
    }

    public boolean isValid() {
        return location1.getExtent().equals(location2.getExtent());
    }

    public boolean inRegion(int blockX, int blockY, int blockZ, World world) {
        return  ((location1.getBlockX() < blockX && location2.getBlockX() > blockX) ||
                        (location1.getBlockX() > blockX && location2.getBlockX() < blockX)) &&
                ((location1.getBlockY() < blockY && location2.getBlockY() > blockY) ||
                        (location1.getBlockY() > blockY && location2.getBlockY() < blockY)) &&
                ((location1.getBlockZ() < blockZ && location2.getBlockZ() > blockZ) ||
                        (location1.getBlockZ() > blockZ && location2.getBlockZ() < blockZ)) &&
                location1.getExtent().equals(world);
    }

    public boolean inRegion(Location<World> location) {
        return inRegion(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getExtent());
    }

    public boolean isOverlapping(Box other) {
        if (!this.location1.getExtent().equals(other.location1.getExtent())) {
            return false;
        }
        for (Vector<Integer> vector : getCorners()) {
            if (other.inRegion(vector.get(0), vector.get(1), vector.get(2), this.location1.getExtent())) {
                return true;
            }
        }
        return false;
    }

    private List<Vector<Integer>> getCorners() {
        List<Vector<Integer>> output = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Vector<Integer> corner = new Vector<>(3);
            corner.set(0, ((i / 4) % 2 == 0) ? location1.getBlockX() : location2.getBlockX());
            corner.set(1, ((i / 2) % 2 == 0) ? location1.getBlockY() : location2.getBlockY());
            corner.set(2, ((i    ) % 2 == 0) ? location1.getBlockZ() : location2.getBlockZ());
            output.set(i, corner);
        }
        return output;
    }

    public Text formatReadable(int id) {
        return Text.of("No readable format available yet.");
    }

}
