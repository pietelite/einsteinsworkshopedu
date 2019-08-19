package me.pietelite.einsteinsworkshopedu.features.mute;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedList;
import java.util.List;

public class MuteManager extends FeatureManager {

    private List<Player> mutedPlayers = new LinkedList<>();
    public boolean isAllMuted = false;

    public MuteManager(EweduPlugin plugin) {
        super(plugin, EweduPlugin.FeatureTitle.MUTE);
    }

    public List<Player> getMutedPlayers() {
        return mutedPlayers;
    }

}
