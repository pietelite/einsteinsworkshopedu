package me.pietelite.einsteinsworkshopedu.features;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.Feature;

import java.util.List;

public abstract class FeatureManager {

    private EweduPlugin plugin;

    public FeatureManager(EweduPlugin plugin, EweduPlugin.FeatureTitle title) {
        this.plugin = plugin;
        addFeature(title);
    }

    private void addFeature(EweduPlugin.FeatureTitle title) {
        plugin.getFeatures().put(title, new Feature(title.name(), this));
    }

    public void save() {
        // To be overwritten
    }

    public void loadData() {
        // To be overwritten
    }

    public List<? extends Object> getElements() {
        return null;
        // To be overwritten
    }

}
