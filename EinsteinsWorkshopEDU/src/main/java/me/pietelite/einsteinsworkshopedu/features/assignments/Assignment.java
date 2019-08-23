package me.pietelite.einsteinsworkshopedu.features.assignments;

import java.util.*;

import me.pietelite.einsteinsworkshopedu.tools.storage.EweduElement;
import me.pietelite.einsteinsworkshopedu.tools.storage.StorageLine;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Assignment implements EweduElement {

	static final int MAXIMUM_TITLE_LENGTH = 30;
	static final int MAXIMUM_BODY_LENGTH = 200;
	
	private static final TextColor DEFAULT_TITLE_COLOR = TextColors.RED;
	private static final TextColor DEFAULT_BODY_COLOR = TextColors.WHITE;
	
	public static final List<String> DEFAULT_ASSIGNMENT_TYPES = Arrays.asList("lesson", "challenge");

	private Text title = Text.EMPTY;
	private Text body = Text.EMPTY;
	private Date timestamp;
	private String type;
	
	private List<UUID> playersCompleted = new LinkedList<>();
	
	Assignment(String type, String title) throws TitleTooLongException, IllegalArgumentException {
		this.setType(type.toUpperCase());
		this.setTitle(title);
		this.timestamp = new Date();
	}
	
	Assignment(String type, String unixTime, String title, String body) throws TitleTooLongException, BodyTooLongException, IllegalArgumentException {
		this(type, title);
		this.setBody(Text.of(DEFAULT_BODY_COLOR, body));
		this.setTimestamp(new Date(Long.parseLong(unixTime)));
	}
	
	Text getCompletableTitle(int id) {
		return Text.builder(title, title.toPlain())
				.onHover(TextActions.showText(Text.of(TextColors.LIGHT_PURPLE, "Click here to toggle completion of this assignment")))
				.onClick(TextActions.runCommand("/einsteinsworkshop assignment complete " + id))
				.build();
	}

	private void setTitle(Text title) throws TitleTooLongException {
		if (title.toPlain().length() > MAXIMUM_TITLE_LENGTH) throw new TitleTooLongException();
		this.title = title;
	}

	void setTitle(String title) throws TitleTooLongException {
		setTitle(Text.builder(title)
					.color(DEFAULT_TITLE_COLOR)
					.style(TextStyles.UNDERLINE)
					.build()
					);
	}

	private void setBody(Text body) throws BodyTooLongException {
		if (body.toPlain().length() > MAXIMUM_BODY_LENGTH) throw new BodyTooLongException();
		this.body = body;
	}
	
	void setBody(String body) throws BodyTooLongException {
		setBody(Text.of(DEFAULT_BODY_COLOR, body));
	}
	
	void setTimestamp(Date date) {
		this.timestamp = date;
	}
	
	List<UUID> getPlayersCompleted() {
		return this.playersCompleted;
	}
	
	private String[] getPlayersCompletedNames() {
		String[] output = new String[playersCompleted.size()];
		for (int i = 0; i < playersCompleted.size(); i++) {
			if (Sponge.getServer().getPlayer(playersCompleted.get(i)).isPresent()) {
				output[i] = Sponge.getServer().getPlayer(playersCompleted.get(i)).get().getName();
			}
		}
		return output;
	}

	void setType(String type) throws IllegalArgumentException {
		this.type = type.toLowerCase();
	}
	
	public Text formatReadable(int id) {
		return Text.builder(id + ". ").color(TextColors.RED)
				.append(Text.of(TextColors.GRAY, "[",
						Text.of(TextColors.AQUA, TextStyles.ITALIC, type.toUpperCase()),
						"] "))
				.append(this.getCompletableTitle(id))
				.append(Text.of(TextColors.GRAY, ": "))
				.append(body)
				.build();
	}
	
	Text formatReadableVerbose(int id) {
		return Text.builder(id + ". ").color(TextColors.RED)
				.append(Text.of(TextColors.GRAY, "[",
						Text.of(TextColors.AQUA, TextStyles.ITALIC, type.toUpperCase()),
						"] "))
				.append(this.getCompletableTitle(id))
				.append(Text.of(TextColors.GRAY, ": "))
				.append(body)
				.append(Text.of(TextColors.YELLOW, "\nLast Edited: ", TextColors.AQUA, timestamp.toString()))
				.append(Text.of(TextColors.YELLOW, "\nCompleted By: ", TextColors.AQUA, String.join(", ", getPlayersCompletedNames())))
				.build();
	}

	@Override
	public StorageLine toStorageLine() {
		return StorageLine.builder()
				.addItem(type)
				.addItem(title.toPlain())
				.addItem(body.toPlain())
				.addItem(String.valueOf(timestamp.getTime()))
				.build();
	}

	static final class TitleTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	static final class BodyTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
}
