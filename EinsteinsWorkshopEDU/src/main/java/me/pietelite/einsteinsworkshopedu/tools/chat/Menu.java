package me.pietelite.einsteinsworkshopedu.tools.chat;

import java.util.LinkedList;
import java.util.List;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Menu {
  private static final TextColor DEFAULT_HEADER_COLOR = TextColors.GREEN;
  private static final Text MENU_HEADER = Text.of(
      TextColors.DARK_PURPLE,
      "EinsteinsWorkshopEDU Menu");
  private List<Section> sections = new LinkedList<>();

  public void addSection(Section component) {
    this.sections.add(component);
  }

  /**
   * Send this menu to the designated source.
   *
   * @param player The player to which to send the menu
   */
  public void sendTo(Player player) {
    player.sendMessage(MENU_HEADER);
    for (Section section : this.sections) {
      if (player.hasPermission(section.permission.toString())) {
        section.sendTo(player);
      }
    }
  }

  public static class Section {
    public static final Menu.Section NONE = new Section(
        Text.EMPTY,
        EweduPlugin.Permissions.STUDENT,
        new LinkedList<>());
    final Text header;
    final EweduPlugin.Permissions permission;
    final List<ClickableMessage.ClickableCommand> clickables;

    /**
     * Create a section of the general command menu.
     *
     * @param header     The header of this menu section
     * @param permission The permission level which is allowed to
     *                   use this command
     * @param clickables A list of clickable encapsulations which will be
     *                   constructed into clickable objects in a message
     *                   once the menu is called
     */
    public Section(Text header,
                   EweduPlugin.Permissions permission,
                   List<ClickableMessage.ClickableCommand> clickables) {
      this.header = header;
      this.permission = permission;
      this.clickables = clickables;
    }

    public Section(String header,
                   EweduPlugin.Permissions permission,
                   List<ClickableMessage.ClickableCommand> clickables) {
      this(Text.of(DEFAULT_HEADER_COLOR, TextStyles.BOLD, header), permission, clickables);
    }

    void sendTo(CommandSource source) {
      source.sendMessage(header);
      ClickableMessage.Builder messageBuilder = ClickableMessage.builder();
      for (ClickableMessage.ClickableCommand clickable : this.clickables) {
        if (source.hasPermission(clickable.getPermission().toString())) {
          messageBuilder.addClickableCommand(clickable);
        }
      }
      source.sendMessage(messageBuilder.build().toText());
    }

  }

}
