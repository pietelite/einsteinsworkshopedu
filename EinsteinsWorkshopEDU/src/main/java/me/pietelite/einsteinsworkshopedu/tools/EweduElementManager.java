package me.pietelite.einsteinsworkshopedu.tools;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElementFactory;
import me.pietelite.einsteinsworkshopedu.tools.storage.Exporter;
import me.pietelite.einsteinsworkshopedu.tools.storage.Importer;

import java.util.List;

public abstract class EweduElementManager<P extends EweduElement> {

    private final EweduElementFactory<P> factory;
    private final Importer<P> importer;
    private final Exporter<P> exporter;
    private final EweduPlugin plugin;
    private List<P> elements;

    public EweduElementManager(EweduPlugin plugin,
                               EweduElementFactory<P> factory,
                               String storageFileName,
                               String defaultStorageAssetFileName) {
        this.factory = factory;
        this.importer = new Importer<>(plugin, storageFileName, factory);
        this.exporter = new Exporter<>(plugin, storageFileName, defaultStorageAssetFileName);
        this.plugin = plugin;
        loadData();
        plugin.getElementManagers().add(this);
    }

    public void save() {
        exporter.store(elements);
    }

    public void loadData() {
        elements = importer.retrieve();
    }

    public List<P> getElements() {
        return elements;
    }

}
