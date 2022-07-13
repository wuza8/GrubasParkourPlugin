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
    public static int getExpMultiplier(ParkourCategory parkourCategory){
        if (parkourCategory == ParkourCategory.NO_CATEGORY)
            return 0;
        if (parkourCategory == ParkourCategory.EASY)
            return 1;
        if (parkourCategory == ParkourCategory.COMMUNITY)
            return 1;
        if (parkourCategory == ParkourCategory.DROPPER)
            return 1;
        if (parkourCategory == ParkourCategory.EVENT)
            return 1;
        if (parkourCategory == ParkourCategory.HARD)
            return 3;
        if (parkourCategory == ParkourCategory.MEDIUM)
            return 2;
        if (parkourCategory == ParkourCategory.SPECIAL)
            return 1;
        if (parkourCategory == ParkourCategory.KZ)
            return 4;
        return 0;
    }
}