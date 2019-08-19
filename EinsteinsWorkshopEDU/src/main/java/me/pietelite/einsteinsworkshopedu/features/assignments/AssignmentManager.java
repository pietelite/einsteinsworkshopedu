package me.pietelite.einsteinsworkshopedu.features.assignments;

import me.pietelite.einsteinsworkshopedu.EweduPlugin;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.TitleTooLongException;
import me.pietelite.einsteinsworkshopedu.features.EweduElementManager;

public class AssignmentManager extends EweduElementManager<Assignment> {
	
	private static final String ASSIGNMENTS_FILE_NAME = "assignments.txt";
	private static final String DEFAULT_FILE_ASSET_NAME = "default_assignments.txt";
	
	public AssignmentManager(EweduPlugin plugin) {
		super(
				plugin,
				EweduPlugin.FeatureTitle.ASSIGNMENTS,
				line -> {
					String[] tokens = line.getTokens();
					if (tokens.length != 4) throw new IllegalArgumentException();
					try {
						return new Assignment(tokens[0],
								tokens[3], // Time
								tokens[1], // Title
								tokens[2]); // Body
					} catch (BodyTooLongException | TitleTooLongException e) {
						e.printStackTrace();
						return null;
					}
				},
				ASSIGNMENTS_FILE_NAME,
				DEFAULT_FILE_ASSET_NAME
		);
	}
}
