package me.pietelite.einsteinsworkshopedu;
import static me.pietelite.einsteinsworkshopedu.EWEDUPlugin.VERSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.TargetEntityEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.SpongeCommandIssuer;
import co.aikar.commands.SpongeCommandManager;
import me.pietelite.einsteinsworkshopedu.freeze.FreezeCommand;
import me.pietelite.einsteinsworkshopedu.freeze.FreezeManager;
import me.pietelite.einsteinsworkshopedu.freeze.UnfreezeCommand;
import me.pietelite.einsteinsworkshopedu.listeners.InteractEventListener;
import me.pietelite.einsteinsworkshopedu.listeners.TargetEntityEventListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

/**
 * The main class for the EDU plugin for Einstein's Workshop.
 * This plugin is made exclusively for Einstein's Workshop
 * Do not use this plugin without explicit approval from a member of staff from
 * Einstein's Workshop.
 * <p>
 * <b>Permissions</b>
 * <p>
 * <li><i>einsteinsworkshop.command</i>: Allows the use of the /einsteinsworkshop command</li>
 * <li><i>einsteinsworkshop.command.freeze</i>: Allows the use of the /einsteinsworkshop freeze command</li>
 * <li><i>einsteinsworkshop.command.unfreeze</i>: Allows the use of the /einsteinsworkshop unfreeze command</li>
 * <li><i>einsteinsworkshop.freezeimmunity</i>: Makes the player immune to freezing</i>
 */
@Plugin(id = "ewedu",
		name = "EinsteinsWorkshopEDU",
		version = VERSION,
		description = "Education Administratrive Tool")
public class EWEDUPlugin {
	
	public static final String VERSION = "1.0";
	
	@Inject
    /** General logger. From Sponge API. */
    private Logger logger;
	
	@Inject
    @DefaultConfig(sharedRoot = false)
    /** Location of the default configuration file for this plugin. From Sponge API. */
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    /** Configuration manager of the configuration file. From Sponge API. */
    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    /** The root node of the configuration file, using the configuration manager. */
    private ConfigurationNode rootNode;
    
    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDirectory;
    
    @Inject
    private PluginContainer container;
    
    private SpongeCommandManager commandManager;
    
    private FreezeManager freezeManager;

    @Listener
    /**
     * Run initialization sequence before the game starts.
     * All classes that other classes depend on must be initialized here.
     * @param event the event run before the game starts
     */
    public void initialize(GamePreInitializationEvent event) {
        logger.info("Initializing GriefAlert...");
        
        freezeManager = new FreezeManager(this);
        
        // Load the config from the Sponge API and set the specific node values.
        initializeConfig();
        
        // Classes which other classes depend on must be initialized here. 
        
        // Register all the listeners with Sponge
        registerListeners();
        
        // Register all the commands with Sponge
        registerCommands();
       
    }

	private void initializeConfig() {
    	if (!defaultConfig.toFile().exists()) {
            logger.info("Generating new Configuration File...");
            try {
                rootNode = configManager.load();
                ConfigurationNode featureNode = rootNode.getNode("features");
                featureNode.getNode("freeze_students").setValue(true);
                configManager.save(rootNode);
                logger.info("New Configuration File created successfully!");
            } catch (IOException e) {
                logger.warn("Exception while reading configuration", e);
            }
        } else {
        	logger.info("Configuration file already exists. Not creating.");
        }
	}
    
	@SuppressWarnings("deprecation")
	private void registerCommands() {
    	commandManager = new SpongeCommandManager(this.container);
    	commandManager.enableUnstableAPI("help");
    	commandManager.createRootCommand("einsteinsworkshop");
    	commandManager.registerCommand(new EinsteinsWorkshopCommand(this));
    	commandManager.registerCommand(new FreezeCommand(this));
    	commandManager.registerCommand(new UnfreezeCommand(this));
    	registerConditions();
    	registerCompletions();
    }
    
    private void registerConditions() {
    	// @Condition("player)
		commandManager.getCommandConditions().addCondition("player", (context) -> {
			SpongeCommandIssuer issuer = context.getIssuer();
			if (!(issuer.isPlayer())) {
				throw new ConditionFailedException("You must be a player to execute this command.");
			}
		});
	}
    
    private void registerCompletions() {
    	commandManager.getCommandCompletions().registerCompletion("players", c -> {
    			List<String> onlinePlayerNames = new ArrayList<String>();
    			for (Player player : Sponge.getServer().getOnlinePlayers()) {
    				onlinePlayerNames.add(player.getName());
    			}
    			return onlinePlayerNames;
    		});
    }


	private void registerListeners() {
		Sponge.getEventManager().registerListener(this, TargetEntityEvent.class, Order.LAST, new TargetEntityEventListener(this));
		Sponge.getEventManager().registerListener(this, InteractEvent.class, Order.LAST, new InteractEventListener(this));
		
	}	

	/**
     * To be run when the plugin reloads
     * @param event The GameReloadEvent
     */
    @Listener
    public void onReload(GameReloadEvent event) {
        logger.info("Reloading EinsteinsWorkshopEDU config data");
        try {
            rootNode = configManager.load();
        } catch (IOException e) {
            logger.warn("Exception while reading configuration", e);
        }
        logger.info("EinsteinsWorkshopEDU config data reloaded!");
    }
    
    public FreezeManager getFreezeManager() {
    	return freezeManager;
    }

}
