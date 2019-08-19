package me.pietelite.einsteinsworkshopedu.tools.storage;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Exporter<P extends EweduElement> {

    private final EweduPlugin plugin;
    private final String defaultAssetName;
    private final Path filePath;

    public Exporter(EweduPlugin plugin, String fileName, String defaultAssetName) {
        this.plugin = plugin;
        this.filePath = Paths.get(plugin.getDataDirectory().getPath(), fileName);
        this.defaultAssetName = defaultAssetName;
        initialize();
    }

    private void initialize() {
        if (Files.notExists(this.filePath)) {
            try {
                if (plugin.getAsset(defaultAssetName).isPresent()) {
                    plugin.getAsset(defaultAssetName).get().copyToFile(filePath);
                    plugin.getLogger().info("File '" + filePath.getFileName().toString() + "' created in " +
                            "EinsteinsWorkshopEDU data folder.");
                } else {
                    plugin.getLogger().error("Default file asset not found.");
                    File file = filePath.toFile();
                    if (file.createNewFile()) {
                        plugin.getLogger().info("Created empty '" + this.filePath.getFileName() + "'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void revert() {
        try {
            if (plugin.getAsset(defaultAssetName).isPresent()) {
                plugin.getAsset(defaultAssetName).get().copyToFile(filePath, true);
            } else {
                File file = filePath.toFile();
                if (!file.delete()) {
                    plugin.getLogger().error("Could not delete '" + filePath.getFileName() + "' when saving a new version of the file.");
                }
                if (!file.createNewFile()) {
                    plugin.getLogger().error("Could not create a new file called '" + filePath.getFileName() + "' to save information.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile() {
        return filePath.toFile();
    }

    public void store(@Nonnull List<P> elements) {
        if (!getFile().exists()) {
            return;
        }
        revert();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath.toFile(), true);
            for (P element : elements) {
                String line = "\n".concat(element.toStorageLine().toString());
                outputStream.write(line.getBytes(), 0, line.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
