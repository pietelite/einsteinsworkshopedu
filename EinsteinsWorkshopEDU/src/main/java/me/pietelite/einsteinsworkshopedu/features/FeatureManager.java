package me.pietelite.einsteinsworkshopedu.features;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;

import java.util.List;

public abstract class FeatureManager {

    protected EweduPlugin plugin;

    public FeatureManager(EweduPlugin plugin) {
        this.plugin = plugin;
    }

    public void save() {
        // To be overwritten
    }

    public void loadData() {
        // To be overwritten
    }

    public List<? extends EweduElement> getElements() {
        return null;
        // To be overwritten
    }

}
