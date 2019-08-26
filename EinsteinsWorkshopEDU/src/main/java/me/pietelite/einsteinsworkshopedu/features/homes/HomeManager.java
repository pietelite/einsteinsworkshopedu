package me.pietelite.einsteinsworkshopedu.features.homes;

import com.flowpowered.math.vector.Vector3d;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.EweduElementManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class HomeManager extends EweduElementManager<Home> {

  private static final String HOME_FILE_NAME = "homes.txt";
  private static final String DEFAULT_HOME_ASSET_FILE = "default_homes.txt";

  HomeManager(EweduPlugin plugin) {
    super(
        plugin,
        line -> {
          String[] tokens = line.getTokens();
          if (tokens.length != 8) {
              throw new IllegalArgumentException();
          }
          try {
            if (Sponge.getServer().getWorld(UUID.fromString(tokens[7])).isPresent()) {
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
            } else {
              throw new NoSuchElementException();
            }
          } catch (NumberFormatException numException) {
            plugin.getLogger().error("The line could not be formed into a Home object because of a "
                + "NumberFormatException: " + String.join(",", tokens));
            return null;
          } catch (NoSuchElementException elemException) {
            plugin.getLogger().error("The line could not be formed into a Home object because of a "
                + "NoSuchElementException: " + String.join(",", tokens));
            return null;
          }
        },
        HOME_FILE_NAME,
        DEFAULT_HOME_ASSET_FILE
    );
  }

  private Home getHome(UUID playerUuid) {
    for (Home home : getElements()) {
      if (home.getPlayerUuid().equals(playerUuid)) {
        return home;
      }
    }
    return null;
  }

  Home getHome(Player player) {
    return getHome(player.getUniqueId());
  }

  private void removeHome(UUID playerUuid) {
    List<Home> toRemove = new LinkedList<>();
    for (Home home : getElements()) {
      if (home.getPlayerUuid().equals(playerUuid)) {
        toRemove.add(home);
      }
    }
    for (Home home : toRemove) {
      getElements().remove(home);
    }
  }

  void removeHome(Player player) {
    removeHome(player.getUniqueId());
  }

  void addHome(Player player) {
    getElements().add(new Home(player.getUniqueId(), player.getTransform()));
  }

  void addHome(Player player, int x, int y, int z) {
    getElements().add(new Home(
        player.getUniqueId(),
        player.getLocation().getExtent(),
        new Vector3d(x, y, z),
        player.getRotation()));
  }

}
