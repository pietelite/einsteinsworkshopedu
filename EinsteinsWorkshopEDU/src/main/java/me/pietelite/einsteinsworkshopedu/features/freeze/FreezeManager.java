package me.pietelite.einsteinsworkshopedu.features.freeze;

import java.util.LinkedList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class FreezeManager {

	private List<Player> frozenPlayers;
	public boolean isAllFrozen = false;
	
	public FreezeManager(EWEDUPlugin plugin) {
		this.frozenPlayers = new LinkedList<Player>();
	}
	
	public List<Player> getFrozenPlayers() {
		return frozenPlayers;
	}
	
}
