/*
 * EinsteinsWorkshopEDU is owned and maintained by Pieter Svenson (PietElite).
 */
package me.pietelite.einsteinsworkshopedu.tools.chat;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class ClickableMessage {

    Text text;

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

        public Builder addClickable(String name, String command, Text hoverMessage) {
            Text clickable = Text.builder()
                    .append(Text.of(TextColors.GOLD, " [", Text.of(TextColors.GRAY, name), "]"))
                    .onClick(TextActions.runCommand(command))
                    .onHover(TextActions.showText(hoverMessage))
                    .build();
            builder.append(clickable);
            return this;
        }

        public ClickableMessage build() {
            return new ClickableMessage(builder.build());
        }

    }

}
