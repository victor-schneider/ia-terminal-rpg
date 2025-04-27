package Utils;

public class JsonCleaner {
    public static String clean(String input) {
        if (input == null) {
            return null;
        }
       
        String cleaned = input.replaceAll("(?s)```(json)?\\s*", "");
        cleaned = cleaned.replaceAll("(?s)```\\s*", "");
        return cleaned.trim();
    }
}
