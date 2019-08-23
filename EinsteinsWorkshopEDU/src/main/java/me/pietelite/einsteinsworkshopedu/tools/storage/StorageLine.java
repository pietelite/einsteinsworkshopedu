package me.pietelite.einsteinsworkshopedu.tools.storage;

import java.util.List;

public class StorageLine {

    private static final String DATA_DELIMITER = ";";
    private static final String COMMENT_DELIMITER = "#";
    private String line;

    StorageLine(List<String> tokens) {
        this.line = String.join(DATA_DELIMITER, tokens);
    }

    StorageLine(String line) {
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

    @SuppressWarnings("unused")
    public String getComment() {
        String[] tokens = line.split(COMMENT_DELIMITER);
        if (tokens.length > 1) {
            String comment = "";
            for (int i = 1; i < tokens.length; i++) {
                comment = comment.concat(tokens[i]);
            }
            return comment;
        } else {
            return "";
        }
    }

    boolean hasData() {
        for (String token : getTokens()) {
            if (!token.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    static String removeDelimiters(String line) {
        return line.replaceAll(";", "").replaceAll("#", "");
    }

}
