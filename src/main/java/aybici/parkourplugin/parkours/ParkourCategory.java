package aybici.parkourplugin.parkours;

public enum ParkourCategory {
    EASY, MEDIUM, HARD, DROPPER, KZ, COMMUNITY, NO_CATEGORY, SPECIAL, EVENT;

    public static boolean contains(String test) {

        for (ParkourCategory c : ParkourCategory.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}