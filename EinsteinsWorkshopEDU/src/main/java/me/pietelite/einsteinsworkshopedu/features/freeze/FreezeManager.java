package me.pietelite.einsteinsworkshopedu.features.freeze;

import java.util.LinkedList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;

public class FreezeManager {

	private List<Player> frozenPlayers;
	public boolean isAllFrozen = false;
	
	public FreezeManager(EweduPlugin plugin) {
		this.frozenPlayers = new LinkedList<Player>();
	}
	
	public List<Player> getFrozenPlayers() {
		return frozenPlayers;
	}
	
}
