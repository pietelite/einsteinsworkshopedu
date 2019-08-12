package me.pietelite.einsteinsworkshopedu.features.assignments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

import me.pietelite.einsteinsworkshopedu.EWEDUPlugin;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.BodyTooLongException;
import me.pietelite.einsteinsworkshopedu.features.assignments.Assignment.TitleTooLongException;

public class AssignmentManager extends LinkedList<Assignment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6610090706659987093L;
	
	private static final String ASSIGNMENTS_FILE_NAME = "assignments.txt";
	
	private File assignmentFile;
	
	private EWEDUPlugin plugin;
	
	public AssignmentManager(EWEDUPlugin plugin) {
		super();
		this.plugin = plugin;
		readAssignmentFile(assignmentFile = loadAssignmentFile(false));
	}

	private File loadAssignmentFile(boolean overwrite) {
		plugin.getLogger().info("Loading Assignment File (" + ASSIGNMENTS_FILE_NAME + ") ...");
    	
		File dataFolder = plugin.getDataDirectory();
    	if (dataFolder.mkdir()) plugin.getLogger().info("Einsteins Workshop data directory created.");
    	
    	// Get the file
    	Path griefAlertFilePath = Paths.get(dataFolder.getPath(), ASSIGNMENTS_FILE_NAME);
        
        if (Files.notExists(griefAlertFilePath) || overwrite) {
        	plugin.getLogger().info("File doesn't exist yet! Trying to create as: " + ASSIGNMENTS_FILE_NAME);
            plugin.getAsset("default_assignments.txt").ifPresent(asset -> {
				try {
					asset.copyToFile(griefAlertFilePath, overwrite);
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
            plugin.getLogger().info("Processing Assignments...");
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
                
                splitLine = line.split(Assignment.DATA_REGEX);
                
                Assignment assignment;
                try {
                	// Try to generate the griefAction with the appropriate color
                	assignment = constructAssignmentFromStorage(splitLine);
                } catch (IllegalArgumentException e) {
                	plugin.getLogger().warn("Error in " + ASSIGNMENTS_FILE_NAME + " with line [" + line + "]");
                	plugin.getLogger().warn(e.getMessage());
                	// Fatal error occurred with this line. Skipping line entirely.
                	continue;
                }
                this.add(assignment);
            }
            scanner.close();
            plugin.getLogger().info("Assignments Processed");
            return true;
        } catch (Exception e) {
            plugin.getLogger().warn("Exception while reading " + ASSIGNMENTS_FILE_NAME, e);
            return false;
        }
	}

	private Assignment constructAssignmentFromStorage(String[] assignmentLine) throws IllegalArgumentException {
		if (assignmentLine.length != 4) throw new IllegalArgumentException("Assignment line needs to have 4 arguments. "
				+ "It has " + assignmentLine.length + ". Skipping Line.");
		try {
			return new Assignment(assignmentLine[0],
					assignmentLine[3], // Time
					assignmentLine[1], // Title
					assignmentLine[2]); // Body
		} catch (BodyTooLongException | TitleTooLongException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean writeToFile() {
		if (!assignmentFile.exists()) {
			return false;
		}
		assignmentFile = loadAssignmentFile(true);
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(assignmentFile, true);
			for (Assignment assignment : this) {
				String line = "\n" + assignment.formatData();
				outputStream.write(line.getBytes(), 0, line.length());
			}
			return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
            	outputStream.close();
            	readAssignmentFile(assignmentFile);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
	}
}
