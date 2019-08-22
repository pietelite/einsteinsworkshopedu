package me.pietelite.einsteinsworkshopedu.features.mute;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MuteManager extends FeatureManager {

    private List<UUID> mutedPlayers = new LinkedList<>();
    public boolean isAllMuted = false;

    public MuteManager(EweduPlugin plugin) {
        super(plugin, EweduPlugin.FeatureTitle.MUTE);
    }

    public List<UUID> getMutedPlayers() {
        return mutedPlayers;
    }

}
