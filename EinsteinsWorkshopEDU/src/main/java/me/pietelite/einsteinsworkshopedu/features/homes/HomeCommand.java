package me.pietelite.einsteinsworkshopedu.features.homes;

import co.aikar.commands.annotation.*;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Syntax;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("home")
@Syntax("/ew home [set|player]")
@CommandPermission("einsteinsworkshop.instructor")
public class HomeCommand extends EinsteinsWorkshopCommand {

    public HomeCommand(EweduPlugin plugin) {
        super(plugin);
    }

    @Default
    @Conditions("player")
    public void onDefault(CommandSource source) {
        Player player = (Player) source;
        Home home = plugin.getHomeManager().getHome(player);
        if (home == null) {
            player.sendMessage(Text.of(TextColors.RED, "You have not yet set a home! Use /ew home set"));
        } else {
            home.teleport(player);
            player.sendMessage(Text.of(TextColors.GREEN, "Welcome home!"));
        }
    }

    @Subcommand("set")
    @Conditions("player")
    public void onSet(CommandSource source) {
        Player player = (Player) source;
        plugin.getHomeManager().removeHome(player);
        plugin.getHomeManager().addHome(player);
        plugin.getHomeManager().save();
        player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
    }

    @Subcommand("set")
    @Conditions("player")
    @CommandPermission("einsteinsworkshop.instructor")
    public void onSetLocation(CommandSource source, int x, int y, int z) {
        Player player = (Player) source;
        plugin.getHomeManager().removeHome(player);
        plugin.getHomeManager().addHome(player, x, y, z);
        plugin.getHomeManager().save();
        player.sendMessage(Text.of(TextColors.GREEN, "Home set!"));
    }

    @Subcommand("player")
    @CommandCompletion("players")
    @Conditions("player")
    @CommandPermission("einsteinsworkshop.instructor")
    public void onPlayer(CommandSource source, String username) {
        Player player = (Player) source;
        if (Sponge.getServer().getPlayer(username).isPresent()) {
            Player targetPlayer = Sponge.getServer().getPlayer(username).get();
            Home home = plugin.getHomeManager().getHome(targetPlayer);
            if (home == null) {
                player.sendMessage(Text.of(TextColors.RED, "That player has not yet set a home!"));
            } else {
                home.teleport(player);
                player.sendMessage(Text.of(
                        TextColors.GREEN, "Teleported to ",
                        TextColors.LIGHT_PURPLE, targetPlayer.getName(),
                        TextColors.GREEN, "'s home."));
            }
        } else {
            player.sendMessage(Text.of(TextColors.RED, "That player could not be found!"));
        }
    }
}
