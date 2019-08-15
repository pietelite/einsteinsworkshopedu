package me.pietelite.einsteinsworkshopedu.features;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

import java.util.Collection;
import java.util.HashMap;

public final class FeatureManager {

    final private HashMap<FeatureName,Feature> features;
    final EweduPlugin plugin;

    public FeatureManager(EweduPlugin plugin) {
        features = new HashMap<>();
        features.put(FeatureName.BOXES, new Feature("boxes"));
        features.put(FeatureName.ASSIGNMENTS, new Feature("assignments"));
        features.put(FeatureName.FREEZE, new Feature("freeze"));
        features.put(FeatureName.HOMES, new Feature("homes"));
        this.plugin = plugin;
    }

    public class Feature {

        public boolean isEnabled;
        public String name;

        Feature(String name) {
            this.name = name;
        }

    }

    public enum FeatureName {
        BOXES,
        ASSIGNMENTS,
        FREEZE,
        HOMES
    }

    public Feature getFeature(FeatureName name) {
        return features.get(name);
    }

    public Collection<Feature> getFeatures() {
        return features.values();
    }
}
