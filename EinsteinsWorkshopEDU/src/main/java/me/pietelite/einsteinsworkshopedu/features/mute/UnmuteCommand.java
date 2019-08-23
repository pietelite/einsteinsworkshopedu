package me.pietelite.einsteinsworkshopedu.features.mute;

import co.aikar.commands.annotation.*;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.chat.ClickableMessage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Syntax;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("unmute|um")
@Syntax("/ew unmute help")
@CommandPermission("einsteinsworkshop.instructor")
public class UnmuteCommand extends EinsteinsWorkshopCommand {

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public UnmuteCommand(EweduPlugin plugin) {
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
            source.sendMessage(commandMessage("/ew unmute|um", "all|a"));
            source.sendMessage(commandMessage("/ew unmute|m", "player|p <player>"));
        }
    }

    @Subcommand("all|a")
    public void onMuteAll(CommandSource source) {
        if (!plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().remove(player.getUniqueId());
        }
        ((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).isAllMuted = false;
        source.sendMessage(
                ClickableMessage
                        .builder(Text.of(TextColors.GREEN, "All players have been unmuted"))
                        .addClickableCommand("Mute", "/ew m all", Text.of(TextColors.LIGHT_PURPLE, "Mute all players"))
                        .build()
                        .getText());
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
                if (((MuteManager) plugin.getFeatures().get(EweduPlugin.FeatureTitle.MUTE).getManager()).getMutedPlayers().remove(onlinePlayer.getUniqueId())) {
                    source.sendMessage(
                            ClickableMessage
                                    .builder(Text.of(TextColors.GREEN, onlinePlayer.getName() + " has been unmuted"))
                                    .addClickableCommand("Mute", "/ew m p " + onlinePlayer.getName(),
                                            Text.of(TextColors.LIGHT_PURPLE, "Mute " + onlinePlayer.getName()))
                                    .addClickableCommand("Mute All", "/ew m a",
                                            Text.of(TextColors.LIGHT_PURPLE, "Mute all players"))
                                    .build()
                                    .getText());
                } else {
                    source.sendMessage(Text.of(TextColors.RED, "That player is not muted!"));
                }
                return;
            }
        }
        source.sendMessage(Text.of(TextColors.RED, "No player was found with that name."));
    }
}