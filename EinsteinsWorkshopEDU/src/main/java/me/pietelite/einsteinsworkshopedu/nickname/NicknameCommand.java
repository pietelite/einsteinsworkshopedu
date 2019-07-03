package me.pietelite.einsteinsworkshopedu.nickname;

import javax.annotation.Syntax;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("nickname|nick")
@Syntax("/ew nickname (none)|set")
@CommandPermission("einsteinsworkshop.command.nickname")
public class NicknameCommand extends EinsteinsWorkshopCommand {

	public NicknameCommand(EWEDUPlugin plugin) {
		super(plugin);
	}
	
	@Default
	@Conditions("player")
	public void onNickname(CommandSource source) {
		Player player = (Player) source;
		if (plugin.getNicknameManager().hasNick(player)) {
			player.sendMessage(Text.builder("Your nickname is ").color(TextColors.GOLD)
					.append(plugin.getNicknameManager().getNick(player).get())
					.build());
		} else {
			player.sendMessage(Text.of(TextColors.RED, "You don't have a nickname yet."));
		}
		for (Key<?> key : player.getDisplayNameData().getKeys()) {
			plugin.getLogger().info("Your current display name is " + key.toString());
		}
	}
	
	@Subcommand("set")
	@Syntax("/ew nickname set")
	@Conditions("player")
	public void onNicknameSet(CommandSource source, String nickname) {
		Player player = (Player) source;
		Nickname previousNickname = plugin.getNicknameManager().putNick(player, nickname);
		if (previousNickname == null) {
			player.sendMessage(Text.builder("Your nickname was set to ").color(TextColors.GOLD)
					.append(Text.of(TextColors.LIGHT_PURPLE, nickname))
					.append(Text.of(TextColors.GOLD, "!"))
					.build());
		} else {
			player.sendMessage(Text.builder("Your nickname was changed from ").color(TextColors.GOLD)
					.append(Text.of(TextColors.YELLOW, previousNickname.get()))
					.append(Text.of(TextColors.GOLD, " to "))
					.append(Text.of(TextColors.LIGHT_PURPLE, nickname))
					.append(Text.of(TextColors.GOLD, "!"))
					.build());
		}
	}
	
}
