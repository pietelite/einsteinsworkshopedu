package me.pietelite.einsteinsworkshopedu.freeze;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class FreezeManager {

	private List<UUID> frozenPlayers;
	private EWEDUPlugin plugin;
	
	public FreezeManager(EWEDUPlugin plugin) {
		this.plugin = plugin;
		this.frozenPlayers = new LinkedList<UUID>();
	}
	
	public boolean isFrozen(Player player) {
		return frozenPlayers.contains(player.getUniqueId());
	}
	
	public boolean freeze(Player player) {
		if (!isFrozen(player)) {
			frozenPlayers.add(player.getUniqueId());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean unfreeze(Player player) {
		return frozenPlayers.remove(player.getUniqueId());
	}
	
}
