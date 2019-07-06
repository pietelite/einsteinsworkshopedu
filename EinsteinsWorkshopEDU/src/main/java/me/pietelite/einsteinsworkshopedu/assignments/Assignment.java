package me.pietelite.einsteinsworkshopedu.assignments;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Assignment {

	private static final int MAXIMUM_TITLE_LENGTH = 20;
	private static final int MAXIMUM_BODY_LENGTH = 100;
	
	private Text title;
	private Text body;
	private long timestamp;
	private AssignmentType type;
	
	public enum AssignmentType {
		LESSON,
		CHALLENGE
	}
	
	public Assignment(String type) {
		this.setType(AssignmentType.valueOf(type));
	}

	public Text getTitle() {
		return title;
	}

	public Assignment setTitle(Text title) throws TitleTooLongException {
		if (title.toPlain().length() > MAXIMUM_TITLE_LENGTH) throw new TitleTooLongException();
		this.title = title;
		return this;
	}

	public Text getBody() {
		return body;
	}

	public Assignment setBody(Text body) throws BodyTooLongException {
		if (body.toPlain().length() > MAXIMUM_BODY_LENGTH) throw new BodyTooLongException();
		this.body = body;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public AssignmentType getType() {
		return type;
	}

	public Assignment setType(AssignmentType type) {
		this.type = type;
		return this;
	}
	
	public Text readable(int id) {
		return Text.builder(String.valueOf(id) + ". ").color(TextColors.RED)
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
