package me.pietelite.einsteinsworkshopedu.assignments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.assignments.Assignment.TitleTooLongException;

public class AssignmentManager extends LinkedList<Assignment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6610090706659987093L;
	
	private static final String ASSIGNMENTS_FILE_NAME = "assignments.txt";
	private static final String ASSIGNMENTS_DATA_REGEX = ";";
	
	private EWEDUPlugin plugin;
	
	public AssignmentManager(EWEDUPlugin plugin) {
		super();
		this.plugin = plugin;
		readAssignmentFile(loadAssignmentFile());
	}

	private File loadAssignmentFile() {
		plugin.getLogger().info("Loading GriefAlert file: " + ASSIGNMENTS_FILE_NAME + " ...");
    	
		File dataFolder = plugin.getDataDirectory();
    	if (dataFolder.mkdir()) plugin.getLogger().info("Grief Alert Configuration directory created.");
    	
    	// Get the file
    	Path griefAlertFilePath = Paths.get(dataFolder.getPath(), ASSIGNMENTS_FILE_NAME);
        
        if (Files.notExists(griefAlertFilePath)) {
        	plugin.getLogger().info("File doesn't exist yet! Trying to create as: " + ASSIGNMENTS_FILE_NAME);
            plugin.getAsset("default_assignments.txt").ifPresent(asset -> {
				try {
					asset.copyToFile(griefAlertFilePath, false);
					plugin.getLogger().info(ASSIGNMENTS_FILE_NAME + " created successfully.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
        }
		return griefAlertFilePath.toFile();
	}
	
	private boolean readAssignmentFile(File file) {
		try {
            plugin.getLogger().info("Bring assignments into the server...");
            Scanner scanner = new Scanner(file);
            String[] splitLine;
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                
                // Skip commented line or empty line
                if (line.startsWith("#") || line.equals("")) {
                    continue;
                }
                
                // Get rid of commented sections
                String[] tokens = line.split("#");
                line = tokens[0];
                
                splitLine = line.split(ASSIGNMENTS_DATA_REGEX);
                
                Assignment assignment;
                try {
                	// Try to generate the griefAction with the appropriate color
                	assignment = constructAssignment(splitLine);
                } catch (IllegalArgumentException e) {
                	plugin.getLogger().warn(ASSIGNMENTS_FILE_NAME + " - " + e.getMessage() + " @ Line: " + line);
                	// Fatal error occurred with this line. Skipping line entirely.
                	continue;
                }
                this.add(assignment);
            }
            scanner.close();
            plugin.getLogger().info("Assignments processed!");
            return true;
        } catch (Exception e) {
            plugin.getLogger().warn("Exception while reading " + ASSIGNMENTS_FILE_NAME, e);
            return false;
        }
	}

	private Assignment constructAssignment(String[] assignmentLine) throws IllegalArgumentException {
		if (assignmentLine.length != 4) throw new IllegalArgumentException();
		try {
			return new Assignment(assignmentLine[0],
					assignmentLine[1],
					assignmentLine[2],
					assignmentLine[3]);
		} catch (BodyTooLongException | TitleTooLongException e) {
			e.printStackTrace();
			return null;
		}
	}

}
