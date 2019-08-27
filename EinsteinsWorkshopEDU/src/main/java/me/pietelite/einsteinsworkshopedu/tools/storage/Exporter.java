package me.pietelite.einsteinsworkshopedu.tools.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.annotation.Nonnull;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class Exporter<P extends EweduElement> {

  private final EweduPlugin plugin;
  private final String defaultAssetName;
  private final Path filePath;

  /**
   * A new Exporter to save data from the game into text files.
   * The information is saved in the EinsteinsWorkshopEDU data folder.
   *
   * @param plugin           The instance of the current plugin
   * @param fileName         The name of the file in which the data is saved
   * @param defaultAssetName The name of the default file to put into
   *                         the data folder. This is mainly used to
   *                         put in some informative headers to the file.
   *                         These assets are saved in the asset folder.
   */
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
          plugin.getLogger().info("File '" + filePath.getFileName().toString() + "' created in "
              + "EinsteinsWorkshopEDU data folder.");
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

  private void reset() {
    try {
      if (plugin.getAsset(defaultAssetName).isPresent()) {
        plugin.getAsset(defaultAssetName).get().copyToFile(filePath, true);
      } else {
        File file = filePath.toFile();
        if (!file.delete()) {
          plugin.getLogger().error("Could not delete '" + filePath.getFileName()
              + "' when saving a new version of the file.");
        }
        if (!file.createNewFile()) {
          plugin.getLogger().error("Could not create a new file called '" + filePath.getFileName()
              + "' to save information.");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File getFile() {
    return filePath.toFile();
  }

  /**
   * Store a list of elements into the text file associated with this Exporter.
   * This method removes all previous data!
   *
   * @param elements The list of elements to put into storage
   */
  public void store(@Nonnull List<P> elements) {
    if (!getFile().exists()) {
      return;
    }
    reset();
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
