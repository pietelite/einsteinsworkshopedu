package me.pietelite.einsteinsworkshopedu.features.welcome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.FeatureManager;

public class WelcomeManager extends FeatureManager {

  private static final String WELCOME_MESSAGE_FILE_NAME = "log_in_message.txt";

  private final List<String> welcomeMessage;

  public WelcomeManager(EweduPlugin plugin) {
    super(plugin);
    welcomeMessage = readWelcomeMessageFile(loadWelcomeMessageFile());
  }

  public List<String> getWelcomeMessage() {
    return welcomeMessage;
  }

  private File loadWelcomeMessageFile() {
    plugin.getLogger().info("Loading Login Message File");

    if (plugin.getConfigDirectory().mkdir()) {
      plugin.getLogger().info("EinsteinsWorkshopEDU Configuration Directory Created");
    }

    // Get the file
    Path filePath = Paths.get(plugin.getConfigDirectory().getPath(), WELCOME_MESSAGE_FILE_NAME);

    if (Files.notExists(filePath)) {
      plugin.getLogger().info("File doesn't exist yet! Trying to create as '" + filePath + "'");
      plugin.getAsset("default_welcome_message.txt").ifPresent(asset -> {
        try {
          asset.copyToFile(filePath, false);
          plugin.getLogger().info("'" + WELCOME_MESSAGE_FILE_NAME + "' created successfully.");
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    return filePath.toFile();
  }

  private List<String> readWelcomeMessageFile(File file) {
    try {
      Scanner scanner = new Scanner(file);
      List<String> lines = new LinkedList<>();
      while (scanner.hasNext()) {
        lines.add(scanner.nextLine());
      }
      scanner.close();
      return lines;
    } catch (Exception e) {
      plugin.getLogger().warn("Exception while loading", e);
      return new LinkedList<>();
    }
  }

}
