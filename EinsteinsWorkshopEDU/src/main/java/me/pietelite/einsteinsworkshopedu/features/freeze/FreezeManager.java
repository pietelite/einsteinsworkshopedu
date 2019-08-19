package me.pietelite.einsteinsworkshopedu.features.freeze;

import java.util.LinkedList;
import java.util.List;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.entity.living.player.Player;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import sun.awt.image.ImageWatched;

public class FreezeManager extends FeatureManager {

	private List<Player> frozenPlayers = new LinkedList<>();
	public boolean isAllFrozen = false;
	
	public FreezeManager(EweduPlugin plugin) {
		super(plugin, EweduPlugin.FeatureTitle.FREEZE);
	}
	
	public List<Player> getFrozenPlayers() {
		return frozenPlayers;
	}
	
}
