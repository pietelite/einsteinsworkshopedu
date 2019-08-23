/*
 * EinsteinsWorkshopEDU is owned and maintained by Pieter Svenson (PietElite).
 */
package me.pietelite.einsteinsworkshopedu.tools.chat;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;

public class ClickableMessage {

    private Text text;

    private ClickableMessage(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public static Builder builder(Text messageBody) {
        return new Builder(messageBody);
    }

    public static class Builder {

        private Text.Builder builder;

        private Builder(Text messageBody) {
            builder = Text.builder().append(messageBody);
        }

        public Builder addClickableCommand(String name, String command, Text hoverMessage) {
            Text clickable = Text.builder()
                    .append(Text.of(TextColors.GOLD, TextStyles.ITALIC, " [", Text.of(TextColors.GRAY, name), "]"))
                    .onClick(TextActions.runCommand(command))
                    .onHover(TextActions.showText(hoverMessage))
                    .build();
            builder.append(clickable);
            return this;
        }

        public Builder addClickableURL(String name, String url) throws MalformedURLException {
            Text clickable = Text.builder()
                    .append(Text.of(TextColors.GOLD, TextStyles.ITALIC, " [", Text.of(TextColors.GRAY, name), "]"))
                    .onClick(TextActions.openUrl(new URL(url)))
                    .onHover(TextActions.showText(Text.of(TextColors.LIGHT_PURPLE, url)))
                    .build();
            builder.append(clickable);
            return this;
        }

        public ClickableMessage build() {
            return new ClickableMessage(builder.build());
        }

    }

}
