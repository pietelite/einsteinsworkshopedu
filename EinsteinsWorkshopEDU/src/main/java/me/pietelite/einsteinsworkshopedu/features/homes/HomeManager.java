package me.pietelite.einsteinsworkshopedu.features.homes;

import com.flowpowered.math.vector.Vector3d;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.EweduElementManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class HomeManager extends EweduElementManager<Home> {

    private static final String HOME_FILE_NAME = "homes.txt";
    private static final String DEFAULT_HOME_ASSET_FILE = "default_homes.txt";

    public HomeManager(EweduPlugin plugin) {
        super(
                plugin,
                EweduPlugin.FeatureTitle.HOMES,
                line -> {
                    String tokens[] = line.getTokens();
                    if (tokens.length != 8) throw new IllegalArgumentException();
                    try {
                        return new Home(
                                UUID.fromString(tokens[0]),
                                Double.parseDouble(tokens[1]),
                                Double.parseDouble(tokens[2]),
                                Double.parseDouble(tokens[3]),
                                Double.parseDouble(tokens[4]),
                                Double.parseDouble(tokens[5]),
                                Double.parseDouble(tokens[6]),
                                Sponge.getServer().getWorld(UUID.fromString(tokens[7])).get()
                        );
                    } catch (NumberFormatException num_e) {
                        plugin.getLogger().error("The line could not be formed into a Home object because of a " +
                                "NumberFormatException: " + String.join(",", tokens));
                        return null;
                    } catch (NoSuchElementException elem_e) {
                        plugin.getLogger().error("The line could not be formed into a Home object because of a " +
                                "NoSuchElementException: " + String.join(",", tokens));
                        return null;
                    }
                },
                HOME_FILE_NAME,
                DEFAULT_HOME_ASSET_FILE);
    }

    public Home getHome(UUID playerUUID) {
        for (Home home : getElements()) {
            if (home.getPlayerUUID().equals(playerUUID)) {
                return home;
            }
        }
        return null;
    }

    public Home getHome(Player player) {
        return getHome(player.getUniqueId());
    }

    public boolean removeHome(UUID playerUUID) {
        List<Home> toRemove = new LinkedList<>();
        boolean changed = false;
        for (Home home : getElements()) {
            if (home.getPlayerUUID().equals(playerUUID)) {
                toRemove.add(home);
            }
        }
        for (Home home : toRemove) {
            getElements().remove(home);
            changed = true;
        }
        return changed;
    }

    public boolean removeHome(Player player) {
        return removeHome(player.getUniqueId());
    }

    public void addHome(Player player) {
        getElements().add(new Home(player.getUniqueId(), player.getTransform()));
    }

    public void addHome(Player player, int x, int y, int z) {
        getElements().add(new Home(
                player.getUniqueId(),
                player.getLocation().getExtent(),
                new Vector3d(x, y, z),
                player.getRotation()));
    }

}
