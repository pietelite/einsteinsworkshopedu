package me.pietelite.einsteinsworkshopedu.features.documentation;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

import java.net.MalformedURLException;
import java.util.Collections;
import javax.annotation.Syntax;

import me.pietelite.einsteinsworkshopedu.EinsteinsWorkshopCommand;
import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.tools.chat.ClickableMessage;
import me.pietelite.einsteinsworkshopedu.tools.chat.Menu;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@CommandAlias("einsteinsworkshop|ew")
@Subcommand("documentation|docs")
@Syntax("/ew documentation|docs help")
@CommandPermission("einsteinsworkshop.instructor")
public class DocumentationCommand extends EinsteinsWorkshopCommand {

  /**
   * Generates an handler for the <i>documentation</i> command.
   *
   * @param plugin The primary instance of the EinsteinsWorkshopEDU plugin
   */
  public DocumentationCommand(EweduPlugin plugin) {
    super(plugin,
        new Menu.Section(
            "Documentation",
            EweduPlugin.Permissions.INSTRUCTOR,
            Collections.singletonList(
                new ClickableMessage.ClickableCommand(
                    "Get Link",
                    EweduPlugin.Permissions.INSTRUCTOR,
                    "/ew documentation",
                    "Get the link to the documentation page for EinsteinsWorkshopEDU"
                )
            ))
    );
  }

  @Default
  void onDocs(CommandSource source) {
    try {
      source.sendMessage(ClickableMessage
          .builder(Text.of(TextColors.YELLOW, "EinsteinsWorkshopEDU Documentation"))
          .addClickableUrl("Click Here", EweduPlugin.DOCUMENTATION_LINK)
          .build().toText());
    } catch (MalformedURLException e) {
      plugin.getLogger().error("Documentation link is broken: " + EweduPlugin.DOCUMENTATION_LINK);
    }
  }
}
