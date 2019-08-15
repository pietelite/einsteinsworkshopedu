package me.pietelite.einsteinsworkshopedu.tools.storage;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Importer<P extends EweduElement> {

    private final EweduPlugin plugin;
    private final Path filePath;
    private final EweduElementFactory<P> factory;

    public Importer(EweduPlugin plugin, String fileName, EweduElementFactory<P> factory) {
        this.plugin = plugin;
        this.filePath = Paths.get(plugin.getDataDirectory().getPath(), fileName);
        this.factory = factory;
    }

    private File getFile() {
        return filePath.toFile();
    }

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
                        plugin.getLogger().warn("This file might be outdated or corrupted. Please delete the file and " +
                                "restart server (Data will be lost)");
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
