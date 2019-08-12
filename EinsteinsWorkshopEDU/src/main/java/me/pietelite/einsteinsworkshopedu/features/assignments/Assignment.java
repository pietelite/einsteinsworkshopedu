package me.pietelite.einsteinsworkshopedu.features.assignments;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Assignment {

	public static final int MAXIMUM_TITLE_LENGTH = 30;
	public static final int MAXIMUM_BODY_LENGTH = 200;
	
	private static final TextColor DEFAULT_TITLE_COLOR = TextColors.RED;
	private static final TextColor DEFAULT_BODY_COLOR = TextColors.WHITE;
	
	public static final List<String> DEFAULT_ASSIGNMENT_TYPES = Arrays.asList("lesson", "challenge");
	
	public static final String DATA_REGEX = ";";
	
	private Text title = Text.EMPTY;
	private Text body = Text.EMPTY;
	private Date timestamp;
	private String type;
	
	private List<Player> playersCompleted = new LinkedList<Player>();
	
	public Assignment(String type, String title) throws TitleTooLongException, IllegalArgumentException {
		this.setType(type.toUpperCase());
		this.setTitle(title);
		this.timestamp = new Date();
	}
	
	public Assignment(String type, String unixTime, String title, String body) throws TitleTooLongException, BodyTooLongException, IllegalArgumentException {
		this(type, title);
		this.setBody(Text.of(DEFAULT_BODY_COLOR, body));
		this.setTimestamp(new Date(Long.parseLong(unixTime)));
	}

	public Text getTitle() {
		return title;
	}
	
	public Text getCompletableTitle(int id) {
		return Text.builder(title, title.toPlain())
				.onHover(TextActions.showText(Text.of(TextColors.LIGHT_PURPLE, "Click here to toggle completion of this assignment")))
				.onClick(TextActions.runCommand("/einsteinsworkshop assignment complete " + id))
				.build();
	}

	public void setTitle(Text title) throws TitleTooLongException {
		if (title.toPlain().length() > MAXIMUM_TITLE_LENGTH) throw new TitleTooLongException();
		this.title = title;
	}
	public void setTitle(String title) throws TitleTooLongException {
		setTitle(Text.builder(title)
					.color(DEFAULT_TITLE_COLOR)
					.style(TextStyles.UNDERLINE)
					.build()
					);
	}

	public Text getBody() {
		return body;
	}

	public void setBody(Text body) throws BodyTooLongException {
		if (body.toPlain().length() > MAXIMUM_BODY_LENGTH) throw new BodyTooLongException();
		this.body = body;
	}
	
	public void setBody(String body) throws BodyTooLongException {
		setBody(Text.of(DEFAULT_BODY_COLOR, body));
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date date) {
		this.timestamp = date;
	}
	
	public List<Player> getPlayersCompleted() {
		return this.playersCompleted;
	}
	
	public String[] getPlayersCompletedNames() {
		String[] output = new String[playersCompleted.size()];
		for (int i = 0; i < playersCompleted.size(); i++) {
			output[i] = playersCompleted.get(i).getName();
		}
		return output;
	}

	public String getType() {
		return type;
	}

	public Assignment setType(String type) throws IllegalArgumentException {
		this.type = type.toLowerCase();
		return this;
	}
	
	public Text formatReadable(int id) {
		return Text.builder(String.valueOf(id) + ". ").color(TextColors.RED)
				.append(Text.of(TextColors.GRAY, "[",
						Text.of(TextColors.AQUA, TextStyles.ITALIC, type.toUpperCase()),
						"] "))
				.append(this.getCompletableTitle(id))
				.append(Text.of(TextColors.GRAY, ": "))
				.append(body)
				.build();
	}
	
	public Text formatReadableVerbose(int id) {
		return Text.builder(String.valueOf(id) + ". ").color(TextColors.RED)
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
	
	public String formatData() {
		return String.join(DATA_REGEX,
				type,
				title.toPlain(),
				body.toPlain(),
				String.valueOf(timestamp.getTime()));
	}
	
	public final class TitleTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	public final class BodyTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
}
