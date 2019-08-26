package me.pietelite.einsteinsworkshopedu.features.mute;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class MuteManager extends FeatureManager {

  private List<UUID> mutedPlayers = new LinkedList<>();
  boolean isAllMuted = false;

  public MuteManager(EweduPlugin plugin) {
    super(plugin);
  }

  public List<UUID> getMutedPlayers() {
    return mutedPlayers;
  }

}
