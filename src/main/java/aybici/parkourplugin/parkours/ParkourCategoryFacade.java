package aybici.parkourplugin.parkours;

import aybici.parkourplugin.FileCreator;
import org.bukkit.Material;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static org.bukkit.Bukkit.getLogger;

public class ParkourCategoryFacade {
    private static HashMap<String, ParkourCategory> categories = new HashMap<>();

    private static ParkourCategory unknown = new ParkourCategory("UNKNOWN", "Unknown", 0, Material.SLIME_BALL, -1,0);
    private static String categoriesFilepath = "categories.txt";

    public static void init(){
        loadCategories();
    }

    public static ParkourCategory get(String name){
        if(categories.containsKey(name)){
            return categories.get(name);
        }
        else return unknown;
    }

    private static void loadCategories() {
        categories.clear();
        if (!new File(categoriesFilepath).exists()){
            getLogger().info("Missing file: " + categoriesFilepath + ", parkour categories are a mess! Create a new category now!");
            return;
        }
        try{
            FileReader fileReader = new FileReader(categoriesFilepath);
            BufferedReader reader = new BufferedReader(fileReader);
            readData(reader);
            reader.close();
            fileReader.close();
        } catch(IOException a){
            System.out.println("E KURDE COS JEST NIE TAK DXD");
            System.out.println(Arrays.toString(a.getStackTrace()));
        }
    }

    private static void readData(BufferedReader reader) throws IOException {
        Long numberOfCategories = Long.parseLong(reader.readLine());

        for(int i=0; i<numberOfCategories; i++){
            String[] data = reader.readLine().split("`");
            addCategory(
                    new ParkourCategory(
                            data[0],
                            data[1],
                            Integer.parseInt(data[2]),
                            Material.getMaterial(data[3]),
                            Integer.parseInt(data[4]),
                            Integer.parseInt(data[5]))
            );
        }

    }

    public static void saveCategories(){
        if (new File(categoriesFilepath).delete()){
            getLogger().info("Nadpisywanie kategorii");
            FileCreator.createFile(categoriesFilepath);
        }
        try{
            FileWriter fileWriter = new FileWriter(categoriesFilepath);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writeData(writer);
            writer.close();
            fileWriter.close();
        } catch(IOException r){
            System.out.println("E KURDE COS JEST NIE TAK DXD");
            System.out.println(Arrays.toString(r.getStackTrace()));
        }
    }

    private static void writeData(BufferedWriter writer) throws IOException{
        writer.write(categories.size()+"\n");

        for(ParkourCategory parkourCategory : getAllCategories()){
            writer.write(parkourCategory.getName()+
                    "`"+
                    parkourCategory.getDisplayName()+
                    "`"+
                    parkourCategory.getXpMultiplier()+
                    "`"+
                    parkourCategory.getCategoryMaterial().name()+
                    "`"+
                    parkourCategory.getBookPosition()+
                    "`"+
                    parkourCategory.getMinLevel()+
                    "\n");
        }
    }

    public static Collection<ParkourCategory> getAllCategories(){
        return categories.values();
    }

    public static void addCategory(ParkourCategory parkourCategory){
        categories.put(parkourCategory.getName(), parkourCategory);
    }

    public static void removeCategory(String name){
        categories.remove(name);
    }
}
