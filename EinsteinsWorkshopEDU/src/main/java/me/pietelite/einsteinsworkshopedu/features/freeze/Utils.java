package me.pietelite.einsteinsworkshopedu.features.freeze;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public abstract class Utils {
	
	public static Text createFreezeAllMessage(boolean freezing) {
		
		return Text.builder(freezing ? "Froze " : "Unfroze ").color(freezing ? TextColors.AQUA : TextColors.GREEN)
    			.append(Text.of(TextColors.LIGHT_PURPLE, "all players"))
    			.append(Text.of(TextColors.AQUA, "."))
    			.append(Text.of(TextColors.GOLD, " ["))
    			.append(Text.builder(freezing ? "Unfreeze" : "Freeze")
    					.color(TextColors.GRAY)
    					.onHover(TextActions.showText(Text.of(TextColors.GOLD, "Click to "
    	    					+ (freezing ? "unfreeze" : "freeze")
    	    					+ " all players")))
    					.onClick(TextActions.runCommand("/einsteinsworkshop "
    	    					+ (freezing ? "unfreeze" : "freeze")
    	    					+ " all"))
    					.build())
    			.append(Text.of(TextColors.GOLD, "]"))
    			.build();
	}
	
	public static Text createFreezePlayerMessage(boolean freezing, String playerName) {
		
		return Text.builder(freezing ? "Froze " : "Unfroze ").color(freezing ? TextColors.AQUA : TextColors.GREEN)
    			.append(Text.of(TextColors.LIGHT_PURPLE, playerName))
    			.append(Text.of(TextColors.AQUA, "."))
    			.append(Text.of(TextColors.GOLD, " ["))
    			.append(Text.builder(freezing ? "Unfreeze" : "Freeze")
    					.color(TextColors.GRAY)
    					.onHover(TextActions.showText(Text.of(TextColors.GOLD, "Click to "
    	    					+ (freezing ? "unfreeze" : "freeze")
    	    					+ " this player")))
    					.onClick(TextActions.runCommand("/einsteinsworkshop "
    	    					+ (freezing ? "unfreeze" : "freeze")
    	    					+ " player " + playerName))
    					.build())
    			.append(Text.of(TextColors.GOLD, "]"))
    			.build();
	}
	
}
