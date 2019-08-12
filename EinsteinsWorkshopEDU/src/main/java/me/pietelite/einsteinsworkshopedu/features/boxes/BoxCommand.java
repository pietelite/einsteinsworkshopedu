package me.pietelite.einsteinsworkshopedu.features.boxes;

import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("box|b")
@Syntax("/ew box create|list|delete")
@CommandPermission("einsteinsworkshop.command.assignment")
public class BoxCommand extends EinsteinsWorkshopCommand {

    public BoxCommand(EWEDUPlugin plugin) {
        super(plugin);
    }

    @Subcommand("list")
    @CommandPermission("einsteinsworkshop.command.box.list")
    public void onList(CommandSource source) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        if (plugin.getBoxManager().getBoxes().isEmpty()) {
            source.sendMessage(Text.of(TextColors.YELLOW, "There are no assignments yet."));
            return;
        }
        source.sendMessage(Text.of(TextColors.GOLD, "-- All Assignments --"));
        for (int i = 0; i < plugin.getBoxManager().getBoxes().size(); i++) {
            Box box = plugin.getBoxManager().getBoxes().get(i);
            source.sendMessage(plugin.getBoxManager().getBoxes().get(i).formatReadable(i + 1));
        }
    }

    @Subcommand("position1|pos1")
    @CommandPermission("einsteinsworkshop.command.box.position")
    public void onPosition1(CommandSource source) {
        onPosition(source, 1);
    }

    @Subcommand("position2|pos2")
    @CommandPermission("einsteinsworkshop.command.box.position")
    public void onPosition2(CommandSource source) {
        onPosition(source, 2);
    }

    public void onPosition(CommandSource source, int num) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        if (!(source instanceof Player)) {
            source.sendMessage(Text.of(TextColors.RED, "Only players may execute that command."));
        }
        Player player = (Player) source;
        if (num == 1) {
            plugin.getBoxManager().position1Map.put(player.getUniqueId(), player.getLocation());
        } else {
            plugin.getBoxManager().position2Map.put(player.getUniqueId(), player.getLocation());
        }
    }

    @Subcommand("create")
    @CommandPermission("einsteinsworkshop.command.box.create")
    public void onCreate(CommandSource source) {
        //TODO: write onCreate function
    }

}
