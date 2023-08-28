package aybici.parkourplugin.parkours;

import org.bukkit.Material;

public class ParkourCategory {

    private String name;
    private String displayName;
    private Material categoryMaterial;

    private int bookPosition;
    private int xpMultiplier;

    private int minLevel;

    public String getName(){
        return name;
    }

    public String getDisplayName(){return displayName;}
    public int getXpMultiplier(){
        return xpMultiplier;
    }

    public Material getCategoryMaterial(){
        return categoryMaterial;
    }

    public int getBookPosition(){
        return bookPosition;
    }

    public int getMinLevel(){ return minLevel;}

    public ParkourCategory(String name, String displayName, int xpMultiplier, Material categoryMaterial, int bookPosition, int minLevel) {
        this.name = name;
        this.displayName = displayName;
        this.xpMultiplier = xpMultiplier;
        this.categoryMaterial = categoryMaterial;
        this.bookPosition = bookPosition;
        this.minLevel = minLevel;
    }
}
