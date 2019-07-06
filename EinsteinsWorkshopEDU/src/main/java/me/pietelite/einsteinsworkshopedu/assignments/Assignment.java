package me.pietelite.einsteinsworkshopedu.assignments;

import java.util.Date;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class Assignment {

	public static final int MAXIMUM_TITLE_LENGTH = 30;
	public static final int MAXIMUM_BODY_LENGTH = 200;
	
	private static final TextColor DEFAULT_TITLE_COLOR = TextColors.YELLOW;
	private static final TextColor DEFAULT_BODY_COLOR = TextColors.WHITE;
	
	private Text title = Text.EMPTY;
	private Text body = Text.EMPTY;
	private Date timestamp;
	private AssignmentType type;
	
	public enum AssignmentType {
		LESSON,
		CHALLENGE
	}
	
	public Assignment(String type, String title) throws TitleTooLongException, IllegalArgumentException {
		this.setType(type.toUpperCase());
		this.setTitle(Text.of(DEFAULT_TITLE_COLOR, title));
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

	public void setTitle(Text title) throws TitleTooLongException {
		if (title.toPlain().length() > MAXIMUM_TITLE_LENGTH) throw new TitleTooLongException();
		this.title = title;
	}
	
	public void setTitle(String title) throws TitleTooLongException {
		setTitle(Text.of(DEFAULT_TITLE_COLOR, title));
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

	public AssignmentType getType() {
		return type;
	}

	public Assignment setType(String type) throws IllegalArgumentException {
		this.type = AssignmentType.valueOf(type.toUpperCase());
		return this;
	}
	
	public Text readable(int id) {
		return Text.builder(String.valueOf(id) + ". ").color(TextColors.RED)
				.append(Text.of(TextColors.GRAY, "[",
						Text.of(TextColors.AQUA, TextStyles.ITALIC, type.toString()),
						"] "))
				.append(title)
				.append(Text.of(TextColors.GRAY, ": "))
				.append(body)
				.build();
	}
	
	public final class TitleTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
	public final class BodyTooLongException extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
}
