package me.pietelite.einsteinsworkshopedu.extras;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Syntax;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("documentation|docs")
@Syntax("/ew documentation|docs help")
@CommandPermission("einsteinsworkshop.instructor")
public class DocumentationCommand extends EinsteinsWorkshopCommand {

    public DocumentationCommand(EweduPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onDocs(CommandSource source) {
        source.sendMessage(Text.of(
                TextColors.GRAY,
                "EinsteinsWorkshopEDU Documentation and Source Code: "));
        source.sendMessage(Text.of(
                TextColors.AQUA,
                "www.github.com/pietelite/einsteinsworkshopedu"));
    }
}
