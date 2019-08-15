package me.pietelite.einsteinsworkshopedu.tools.storage;

import java.util.List;

public class StorageLine {

    private static final String DATA_DELIMITER = ";";
    private static final String COMMENT_DELIMITER = "#";
    private String line;

    public StorageLine(List<String> tokens) {
        this.line = String.join(DATA_DELIMITER, tokens);
    }

    public StorageLine(String line) {
        this.line = line;
    }

    public static StorageLineBuilder builder() {
        return new StorageLineBuilder();
    }

    public String toString() {
        return line;
    }

    public String[] getTokens() {
        String[] macroTokens = line.split(COMMENT_DELIMITER);
        String data = macroTokens[0];
        return data.split(DATA_DELIMITER);
    }

    public String getComment() {
        String[] tokens = line.split(COMMENT_DELIMITER);
        if (tokens.length > 1) {
            String comment = "";
            for (int i = 1; i < tokens.length; i++) {
                comment.concat(tokens[i]);
            }
            return comment;
        } else {
            return "";
        }
    }

    public boolean hasData() {
        for (String token : getTokens()) {
            if (!token.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static String removeDelimiters(String line) {
        return line.replaceAll(";", "").replaceAll("#", "");
    }

}
