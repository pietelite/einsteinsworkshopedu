package me.pietelite.einsteinsworkshopedu.features.boxes;

import javax.annotation.Nonnull;
import javax.annotation.Syntax;

import co.aikar.commands.annotation.Conditions;
import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;
import me.pietelite.einsteinsworkshopedu.tools.SimpleLocation;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.World;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("box|b")
@Syntax("/ew box create|list|delete")
@CommandPermission("einsteinsworkshop.instructor")
public class BoxCommand extends EinsteinsWorkshopCommand {

    public BoxCommand(EWEDUPlugin plugin) {
        super(plugin);
    }

    @Subcommand("list")
    public void onList(CommandSource source) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        if (plugin.getBoxManager().getBoxes().isEmpty()) {
            source.sendMessage(Text.of(TextColors.YELLOW, "There are no boxes yet."));
            return;
        }
        source.sendMessage(Text.of(TextColors.GOLD, "-- All Boxes --"));
        for (int i = 0; i < plugin.getBoxManager().getBoxes().size(); i++) {
            source.sendMessage(plugin.getBoxManager().getBoxes().get(i).formatReadable(i + 1));
        }
    }

    @Subcommand("position1|pos1")
    @Conditions("player")
    public void onPosition1(CommandSource source) {
        onPosition(source, Box.Position.POSITION1);
        source.sendMessage(Text.of(TextColors.GREEN, "Position 1 set!"));
    }

    @Subcommand("position2|pos2")
    @Conditions("player")
    public void onPosition2(CommandSource source) {
        onPosition(source, Box.Position.POSITION2);
        source.sendMessage(Text.of(TextColors.GREEN, "Position 2 set!"));
    }

    private void onPosition(CommandSource source, @Nonnull Box.Position position) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        if (position == Box.Position.POSITION1) {
            plugin.getBoxManager().getPosition1Map().put(
                    player.getUniqueId(),
                    new SimpleLocation(player.getTransform().getPosition(), player.getWorld())
            );
        } else {
            plugin.getBoxManager().getPosition2Map().put(
                    player.getUniqueId(),
                    new SimpleLocation(player.getTransform().getPosition(), player.getWorld())
            );
        }
    }

    @Subcommand("create")
    @Conditions("player")
    public void onCreate(CommandSource source) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        if (!plugin.getBoxManager().hasSelection(player.getUniqueId())) {
            player.sendMessage(Text.of(TextColors.RED, "You don't have a selection!"));
            return;
        }
        SimpleLocation position1 = plugin.getBoxManager().getPosition1Map().get(player.getUniqueId());
        SimpleLocation position2 = plugin.getBoxManager().getPosition2Map().get(player.getUniqueId());
        if (!position1.getWorld().equals(position2.getWorld())) {
            player.sendMessage(Text.of(TextColors.RED, "Your two selection points must be in the same world."));
        } else {
            Box newBox = new Box(position1.getPosition(), position2.getPosition(), position1.getWorld());

            for (Box box : plugin.getBoxManager().getBoxes()) {
                if (box.isOverlapping(newBox)) {
                    player.sendMessage(Text.of(TextColors.RED, "Your selection cannot overlap with another box."));
                    return;
                }
            }

            plugin.getBoxManager().getBoxes().add(newBox);
            plugin.getBoxManager().saveBoxes();
            player.sendMessage(Text.of(TextColors.GREEN, "Box added!"));
        }

    }

    @Subcommand("destroy")
    public void onDestroy(CommandSource source, int id) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            plugin.getBoxManager().getBoxes().remove(id - 1);
            plugin.getBoxManager().saveBoxes();
            player.sendMessage(Text.of(TextColors.GREEN, "Box ", TextColors.GOLD, id, TextColors.GREEN, " removed!"));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

    @Subcommand("info")
    public void onInfo(CommandSource source, int id) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            player.sendMessage(plugin.getBoxManager().getBoxes().get(id - 1).formatReadableVerbose(id));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

    @Subcommand("info")
    @Conditions("player")
    public void onInfo(CommandSource source) {
        Player player = (Player) source;
        for (int i = 0; i < plugin.getBoxManager().getBoxes().size(); i++) {
            if (plugin.getBoxManager().getBoxes().get(i).contains(player)) {
                onInfo(player, i + 1);
            }
        }
    }

    @Subcommand("edit movement")
    @CommandPermission("einsteinsworkshop.instructor")
    public void onEditMovement(CommandSource source, int id, boolean value) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            if (plugin.getBoxManager().getBoxes().get(id - 1).movement == value) {
                player.sendMessage(Text.of(
                        TextColors.YELLOW, "Student mobility was already set to ",
                        TextColors.DARK_PURPLE, value,
                        TextColors.YELLOW, "!"));
            } else {
                plugin.getBoxManager().getBoxes().get(id - 1).movement = value;
                plugin.getBoxManager().saveBoxes();
                player.sendMessage(Text.of(TextColors.GREEN, "Student mobility set!"));
            }
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

    @Subcommand("edit building")
    public void onEditBuilding(CommandSource source, int id, boolean value) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            if (plugin.getBoxManager().getBoxes().get(id - 1).building == value) {
                player.sendMessage(Text.of(
                        TextColors.YELLOW, "Student building ability was already set to ",
                        TextColors.DARK_PURPLE, value,
                        TextColors.YELLOW, "!"));
            } else {
                plugin.getBoxManager().getBoxes().get(id - 1).building = value;
                plugin.getBoxManager().saveBoxes();
                player.sendMessage(Text.of(TextColors.GREEN, "Student building ability set!"));
            }
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

    @Subcommand("show")
    @Conditions("player")
    public void onShow(CommandSource source, int id) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            plugin.getBoxManager().getBoxes().get(id - 1).spawnBorderParticles(player, Color.GREEN);
            player.sendMessage(Text.of(
                    TextColors.GREEN, "Showing box ",
                    TextColors.GOLD, id,
                    TextColors.GREEN, "."));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

    @Subcommand("show all")
    @Conditions("player")
    public void onShowAll(CommandSource source) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        for (Box box : plugin.getBoxManager().getBoxes()) {
            box.spawnBorderParticles(player, Color.GREEN);
        }
        player.sendMessage(Text.of(TextColors.GREEN, "Showing all boxes"));
    }

    @Subcommand("teleport|tp")
    @Conditions("player")
    public void onTeleport(CommandSource source, int id) {
        if (!plugin.getFeatureManager().getFeature(FeatureManager.FeatureName.BOXES).isEnabled) {
            source.sendMessage(Text.of(TextColors.RED, "This feature has been disabled."));
            return;
        }
        Player player = (Player) source;
        try {
            Box box = plugin.getBoxManager().getBoxes().get(id - 1);
            player.setTransform(new Transform<>(
                    player.getWorld(),
                    new Vector3d(box.getXMin(), box.getYMin(), box.getZMin()),
                    player.getRotation()
            ));
            player.sendMessage(Text.of(
                    TextColors.GREEN, "Teleported to box ",
                    TextColors.GOLD, id,
                    TextColors.GREEN, "."));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Text.of(TextColors.RED, "No box matches with that id!"));
        }
    }

}
