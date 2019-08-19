package me.pietelite.einsteinsworkshopedu.features;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElementFactory;
import me.pietelite.einsteinsworkshopedu.tools.storage.Exporter;
import me.pietelite.einsteinsworkshopedu.tools.storage.Importer;

import java.util.List;

public abstract class EweduElementManager<P extends EweduElement> extends FeatureManager {

    private final EweduElementFactory<P> factory;
    private final Importer<P> importer;
    private final Exporter<P> exporter;
    private List<P> elements;

    public EweduElementManager(EweduPlugin plugin,
                               EweduPlugin.FeatureTitle title,
                               EweduElementFactory<P> factory,
                               String storageFileName,
                               String defaultStorageAssetFileName) {
        super(plugin, title);
        this.factory = factory;
        this.importer = new Importer<>(plugin, storageFileName, factory);
        this.exporter = new Exporter<>(plugin, storageFileName, defaultStorageAssetFileName);
        loadData();
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
