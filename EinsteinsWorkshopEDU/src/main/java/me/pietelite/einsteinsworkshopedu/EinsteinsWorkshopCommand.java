package me.pietelite.einsteinsworkshopedu;

import co.aikar.commands.annotation.Conditions;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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
			source.sendMessage(commandMessage("/ew", "freeze", ""));
			source.sendMessage(commandMessage("/ew", "unfreeze", ""));
			source.sendMessage(commandMessage("/ew", "box", ""));
    	}
    	if (source.hasPermission("einsteinsworkshop.student"))
			source.sendMessage(commandMessage("/ew", "assignment",""));
	}

    public static Text commandMessage(String command, String subcommand, String description) {
    	return Text.builder(command).color(TextColors.GRAY)
				.append(Text.of(" "))
				.append(Text.of(TextColors.AQUA, subcommand))
				.append((description.isEmpty()) ? Text.of() : Text.of(TextColors. GRAY, ": ", TextColors.WHITE, description))
				.build();
	}


}
