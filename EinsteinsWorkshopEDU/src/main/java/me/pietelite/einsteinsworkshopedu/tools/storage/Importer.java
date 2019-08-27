package me.pietelite.einsteinsworkshopedu.tools.storage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class Importer<P extends EweduElement> {

  private final EweduPlugin plugin;
  private final Path filePath;
  private final EweduElementFactory<P> factory;

  /**
   * A new Importer to scrape data from a specific file in the
   * plugin's data folder.
   *
   * @param plugin   The instance of the current plugin
   * @param fileName The name of the file in which the data is saved
   * @param factory  A factory for the data type which can construct
   *                 an object of type 'P'
   */
  public Importer(EweduPlugin plugin, String fileName, EweduElementFactory<P> factory) {
    this.plugin = plugin;
    this.filePath = Paths.get(plugin.getDataDirectory().getPath(), fileName);
    this.factory = factory;
  }

  private File getFile() {
    return filePath.toFile();
  }

  /**
   * Scrapes all the information from the designated data file.
   *
   * @return A list of the saved objects
   */
  public List<P> retrieve() {
    List<P> elements = new LinkedList<>();
    try {
      Scanner scanner = new Scanner(getFile());
      while (scanner.hasNext()) {
        StorageLine line = new StorageLine(scanner.nextLine());
        if (line.hasData()) {
          try {
            elements.add(factory.construct(line));
          } catch (IllegalArgumentException e) {
            plugin.getLogger().warn("Error reading '" + filePath.getFileName() + "'.");
            plugin.getLogger().warn("The following line could not be read: " + line.toString());
            plugin.getLogger().warn("This file might be outdated or corrupted. "
                + "Please delete the file and restart server (Data will be lost!) "
                + "or contact EinsteinsWorkshopEDU support.");
          }
        }
      }
      scanner.close();
      plugin.getLogger().info("'" + filePath.getFileName() + "' processed");
    } catch (Exception e) {
      plugin.getLogger().error("Exception while reading " + filePath.getFileName(), e);
    }
    return elements;
  }

}
