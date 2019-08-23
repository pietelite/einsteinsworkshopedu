package me.pietelite.einsteinsworkshopedu;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@CommandAlias("einsteinsworkshop|ew")
public class EinsteinsWorkshopCommand extends BaseCommand {

	/** The main plugin object. */
    protected final EweduPlugin plugin;

    /**
     * Basic constructor.
     * @param plugin The main plugin object.
     */
    public EinsteinsWorkshopCommand(EweduPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSource source, CommandHelp help){
		if (source.hasPermission("einsteinsworkshop.instructor")) {
			source.sendMessage(commandMessage("/ew", "freeze|f"));
			source.sendMessage(commandMessage("/ew", "unfreeze|uf"));
			source.sendMessage(commandMessage("/ew", "box|b"));
			source.sendMessage(commandMessage("/ew", "documentation|docs"));
    	}
    	if (source.hasPermission("einsteinsworkshop.student"))
			source.sendMessage(commandMessage("/ew", "assignment|a"));
    		source.sendMessage(commandMessage("/ew", "home|h"));
	}

    protected static Text commandMessage(String command, String subcommand) {
    	return Text.builder(command).color(TextColors.GRAY)
				.append(Text.of(" "))
				.append(Text.of(TextColors.AQUA, subcommand))
				.build();
	}


}
