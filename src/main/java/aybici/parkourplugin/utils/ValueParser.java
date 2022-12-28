package aybici.parkourplugin.utils;

public class ValueParser {
    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Double tryParseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Boolean tryParseBoolean(String text) {
        try {
            return Boolean.parseBoolean(text);
        } catch (Exception e) {
            return null;
        }
    }
}
