package me.pietelite.einsteinsworkshopedu.features.freeze;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class FreezeManager extends FeatureManager {

	private List<UUID> frozenPlayers = new LinkedList<>();
	public boolean isAllFrozen = false;
	
	public FreezeManager(EweduPlugin plugin) {
		super(plugin, EweduPlugin.FeatureTitle.FREEZE);
	}
	
	public List<UUID> getFrozenPlayers() {
		return frozenPlayers;
	}
	
}
