package me.pietelite.einsteinsworkshopedu.features;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public final class FeatureManager {

    final private HashMap<FeatureName,Feature> features;
    final EWEDUPlugin plugin;

    public FeatureManager(EWEDUPlugin plugin) {
        features = new HashMap<FeatureName, Feature>();
        features.put(FeatureName.BOXES, new Feature("boxes"));
        features.put(FeatureName.ASSIGNMENTS, new Feature("assignments"));
        features.put(FeatureName.FREEZE, new Feature("freeze"));
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
        FREEZE
    }

    public Feature getFeature(FeatureName name) {
        return features.get(name);
    }

    public Collection<Feature> getFeatures() {
        return features.values();
    }
}
