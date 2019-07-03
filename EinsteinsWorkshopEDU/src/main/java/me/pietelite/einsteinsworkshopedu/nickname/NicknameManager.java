package me.pietelite.einsteinsworkshopedu.nickname;

import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;

public class NicknameManager {

	private HashMap<UUID, Nickname> nicknames;
	
	private EWEDUPlugin plugin;
	
	public NicknameManager(EWEDUPlugin plugin) {
		this.plugin = plugin;
		this.nicknames = new HashMap<UUID, Nickname>();
	}
	
	public Nickname putNick(Player player, String nickname) {
		return nicknames.put(player.getUniqueId(), new Nickname(player, Text.of(nickname)));
	}
	
	public Nickname getNick(Player player) {
		return nicknames.get(player.getUniqueId());
	}
	
	public Nickname removeNick(Player player) {
		return nicknames.remove(player.getUniqueId());
	}
	
	public boolean hasNick(Player player) {
		return nicknames.containsKey(player.getUniqueId());
	}
	
}
