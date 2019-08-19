package me.pietelite.einsteinsworkshopedu.tools;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class Feature {

    public boolean isEnabled;
    private final String name;
    private final FeatureManager manager;

    public Feature(String name, FeatureManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public FeatureManager getManager() {
        return manager;
    }

}
