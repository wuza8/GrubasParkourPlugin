package aybici.parkourplugin.parkours;

import java.util.HashMap;
import java.util.List;

public class TagsFacade {

    private static HashMap<Parkour, List<Tag>> parkourTags = new HashMap<>();

    public static void init() {

    }

    public static List<Tag> getTagList(Parkour parkour) {
        return null;
    }

    public static boolean addTagToParkour(Parkour parkour, String tagName) {
        return false;
    }

    public static void addNewTag(String tag, String description) {

    }

    private static void saveTags() {

    }

    private static void loadTags() {

    }
}
