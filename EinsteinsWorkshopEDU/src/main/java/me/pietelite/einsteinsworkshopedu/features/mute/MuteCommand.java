package me.pietelite.einsteinsworkshopedu.features.mute;

import co.aikar.commands.annotation.*;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.freeze.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Syntax;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("mute|m")
@Syntax("/ew mute help")
@CommandPermission("einsteinsworkshop.instructor")
public class MuteCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public MuteCommand(EweduPlugin plugin) {
        super(plugin);
    }

    @Default
    @Subcommand("help")
    public void onHelp(CommandSource source) {
        if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        if (source.hasPermission("einsteinsworkshop.instructor")) {
            source.sendMessage(commandMessage("/ew mute|m", "all|a", ""));
            source.sendMessage(commandMessage("/ew mute|m", "player|p <player>", ""));
        }
    }

    @Subcommand("all|a")
    public void onMuteAll(CommandSource source) {
        if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            if (!player.hasPermission("einsteinsworkshop.immunity")) {
                if (!((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().contains(player)) {
                    ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().add(player);
                    player.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
                }
            }
        }
        ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).isAllMuted = true;
        source.sendMessage(Utils.createFreezeAllMessage(true));
    }

    @Subcommand("player|p")
    @CommandCompletion("@players")
    public void onMutePlayer(CommandSource source, String name) {
        if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        for (Player onlinePlayer : Sponge.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getDisplayNameData().displayName().get().toPlain().equalsIgnoreCase(name)) {
                if (!onlinePlayer.hasPermission("einsteinsworkshop.immunity")) {
                    if (!((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().contains(onlinePlayer)) {
                        ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().add(onlinePlayer);
                        source.sendMessage(Utils.createFreezePlayerMessage(true, name));
                        onlinePlayer.sendMessage(Text.of(TextColors.AQUA, "You have been frozen!"));
                    } else {
                        source.sendMessage(Text.of(TextColors.RED, "That player is already frozen!"));
                    }
                } else {
                    source.sendMessage(Text.of(TextColors.RED, "This player cannot be frozen!"));
                }
                return;
            }
        }
        source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
}