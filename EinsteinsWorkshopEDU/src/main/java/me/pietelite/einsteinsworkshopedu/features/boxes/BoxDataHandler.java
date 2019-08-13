package me.pietelite.einsteinsworkshopedu.features.boxes;

import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment;
import org.spongepowered.api.Sponge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BoxDataHandler {

    // In storage, Box line looks like
    // World;position1X;position1Y;position2Z;position2X;position2Y;position2Z;movement;building

    private static final String BOXES_FILE_NAME = "boxes.txt";

    private File boxesFile;

    private EWEDUPlugin plugin;

    public BoxDataHandler(EWEDUPlugin plugin) {
        super();
        this.plugin = plugin;
        boxesFile = loadBoxesFile(false);
    }

    private File loadBoxesFile(boolean overwrite) {
        File dataFolder = plugin.getDataDirectory();
        if (dataFolder.mkdir()) plugin.getLogger().info("Einsteins Workshop data directory created.");

        // Get the file
        Path filePath = Paths.get(dataFolder.getPath(), BOXES_FILE_NAME);

        if (Files.notExists(filePath) || overwrite) {
            plugin.getAsset("default_boxes.txt").ifPresent(asset -> {
                try {
                    asset.copyToFile(filePath, overwrite);
                    plugin.getLogger().info(BOXES_FILE_NAME + " updated successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return filePath.toFile();
    }

    List<Box> readBoxesFile() {
        List<Box> boxes = new LinkedList<Box>();
        try {
            plugin.getLogger().info("Processing Boxes...");
            Scanner scanner = new Scanner(boxesFile);
            String[] splitLine;
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();

                // Skip commented line or empty line
                if (line.startsWith("#") || line.equals("")) {
                    continue;
                }

                // Get rid of commented sections
                String[] tokens = line.split("#");
                line = tokens[0];

                splitLine = line.split(Assignment.DATA_REGEX);

                try {
                    // Try to generate the griefAction with the appropriate color
                    boxes.add(constructBoxFromStorage(splitLine));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warn("Error in " + BOXES_FILE_NAME + " with line [" + line + "]");
                    plugin.getLogger().warn(e.getMessage());
                    // Fatal error occurred with this line. Skipping line entirely.
                    continue;
                }
            }
            scanner.close();
            plugin.getLogger().info("Boxes Processed");
        } catch (Exception e) {
            plugin.getLogger().warn("Exception while reading " + BOXES_FILE_NAME, e);
        }
        return boxes;
    }

    private Box constructBoxFromStorage(String[] boxesLine) throws IllegalArgumentException {
        if (boxesLine.length != 9) throw new IllegalArgumentException("Assignment line needs to have 9 arguments. "
                + "It has " + boxesLine.length + ". Skipping Line.");
        try {
            return new Box(
                    new Vector3d(
                            Float.parseFloat(boxesLine[1]),
                            Float.parseFloat(boxesLine[2]),
                            Float.parseFloat(boxesLine[3])),
                    new Vector3d(
                            Float.parseFloat(boxesLine[4]),
                            Float.parseFloat(boxesLine[5]),
                            Float.parseFloat(boxesLine[6])),
                    UUID.fromString(boxesLine[0]),
                    Boolean.parseBoolean(boxesLine[7]),
                    Boolean.parseBoolean(boxesLine[8]));
        } catch (NumberFormatException num_e) {
            plugin.getLogger().error("The line could not be formed into a Box object because of a " +
                    "NumberFormatException: " + String.join(",", boxesLine));
            return null;
        } catch (NoSuchElementException elem_e) {
            plugin.getLogger().error("The line could not be formed into a Box object because of a " +
                    "NoSuchElementException: " + String.join(",", boxesLine));
            return null;
        }
    }

    public boolean writeToFile(List<Box> boxes) {
        if (!boxesFile.exists()) {
            return false;
        }
        boxesFile = loadBoxesFile(true);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(boxesFile, true);
            for (Box box : boxes) {
                String line = "\n" + box.formatStorage();
                outputStream.write(line.getBytes(), 0, line.length());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
