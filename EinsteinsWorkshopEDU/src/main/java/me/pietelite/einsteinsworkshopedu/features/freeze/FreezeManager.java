package me.pietelite.einsteinsworkshopedu.features.freeze;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class FreezeManager extends FeatureManager {

  private List<UUID> frozenPlayers = new LinkedList<>();
  boolean isAllFrozen = false;

  public FreezeManager(EweduPlugin plugin) {
    super(plugin);
  }

  public List<UUID> getFrozenPlayers() {
    return frozenPlayers;
  }

}
