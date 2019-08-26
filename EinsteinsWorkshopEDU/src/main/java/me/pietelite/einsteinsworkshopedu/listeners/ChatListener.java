package me.pietelite.einsteinsworkshopedu.listeners;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.freeze.FreezeManager;
import me.pietelite.einsteinsworkshopedu.features.mute.MuteManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ChatListener implements EventListener<MessageChannelEvent.Chat> {

  private EweduPlugin plugin;

  public ChatListener(EweduPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void handle(Chat event) {
    Object root = event.getCause().root();
    if (root instanceof Player) {
      Player player = (Player) root;
      if (((FreezeManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.FREEZE).getManager())
          .getFrozenPlayers().contains(player.getUniqueId())) {
        player.sendMessage(Text.of(TextColors.RED, "You cannot chat while frozen!"));
        event.setCancelled(true);
      }
      if (((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager())
          .getMutedPlayers().contains(player.getUniqueId())) {
        player.sendMessage(Text.of(TextColors.RED, "You cannot chat while muted!"));
        event.setCancelled(true);
      }
    }
  }

}
