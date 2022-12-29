package aybici.parkourplugin.parkours;

import aybici.parkourplugin.FileCreator;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.fails.FailSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getLogger;

public class Parkour{
    private String name;
    private Location location;
    private Set<Material> backBlocks = new HashSet<>();
    private TopList topList = new TopList(this);
    public String dataFileNameInsideFolder;
    public String folderName;
    private int identifier;
    private ParkourCategory category;
    private FailSet failSet;
    private String description = "No description.";
    private List<Location> checkpoints = new ArrayList<>();
    private long exp;
    public FinishExpSource finishExpSource;
    public boolean isTopListLoaded;

    Parkour(String name, Location location){
        this.name = name;
        this.location = location.clone();
        this.dataFileNameInsideFolder = File.separator + "parkourData.txt";
        this.folderName = ParkourPlugin.parkourSet.parkoursFolder + File.separator + "parkourMap_" + getName();
        this.category = ParkourCategory.NO_CATEGORY;
        this.identifier = 0;
        this.identifier = generateID();
        this.exp = 0;
        this.finishExpSource = FinishExpSource.DEFAULT;
        this.isTopListLoaded = true;
    }
    Parkour(String name){
        this.name = name;
        this.dataFileNameInsideFolder = File.separator + "parkourData.txt";
        this.folderName = ParkourPlugin.parkourSet.parkoursFolder +File.separator +  "parkourMap_" + getName();
        this.category = ParkourCategory.NO_CATEGORY;
        this.exp = 0;
        this.finishExpSource = FinishExpSource.DEFAULT;
        this.isTopListLoaded = false;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp, boolean refreshPlayersExp) {
        if(refreshPlayersExp)
            ExpManager.refreshExpOfPlayers(this, exp);
        this.exp = exp;
        saveParkour(folderName + dataFileNameInsideFolder);
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
        saveParkour(folderName + dataFileNameInsideFolder);
    }
    public FailSet getFailSetObject(){
        return failSet;
    }
    public void setFailSet(FailSet failSet){
        this.failSet = failSet;
    }
    public int getIdentifier(){
        return this.identifier;
    }
    public void setIdentifier(int identifier){
        this.identifier = identifier;
        saveParkour(folderName + dataFileNameInsideFolder);
    }
    public ParkourCategory getCategory(){
        return category;
    }

    public void setName(String name){
        File file = new File(folderName);
        this.name = name;
        this.folderName = ParkourPlugin.parkourSet.parkoursFolder + File.separator + "parkourMap_" + getName();
        file.renameTo(new File(folderName));
        saveParkour(folderName + dataFileNameInsideFolder);
    }
    public void setCategory(ParkourCategory category){
        this.category = category;
        this.identifier = 0;
        this.identifier = generateID();
        saveParkour(folderName + dataFileNameInsideFolder);
    }

    public TopList getTopListObject(){
        return topList;
    }

    public Location getLocation(){
        if(location != null)
            return location.clone();
        else return null;
    }

    public void setLocation(Location location, boolean autosave){
        this.location = location.clone();
        if(autosave)
        saveParkour(folderName + dataFileNameInsideFolder);
    }

    public String getName() {
        return name;
    }

    public Material addBackBlock(String materialName){
        Material material = getMaterial(materialName);

        if(material == Material.AIR)
            throw new IllegalStateException("AIR can't be a backblock!");
        else if(hasBackBlock(material))
            throw new IllegalStateException(material.name() + " is already a backblock!");

        addBackBlock(material); // file changing in command section
        return material;
    }

    public void addBackBlock(Material material) {
        backBlocks.add(material);
    }

    public Material removeBackBlock(String materialName){
        Material material = getMaterial(materialName);

        if(!hasBackBlock(material))
            throw new IllegalStateException(material.name() + " is not a backblock!");

        removeBackBlock(material); // file changing in command section
        return material;
    }

    public void removeBackBlock(Material material) {
        backBlocks.remove(material);
    }

    public boolean hasAnyBackBlock(Set<Material> materialList) {
        for (Material material : materialList){
            if (backBlocks.contains(material)) return true;
        }
        return false;
    }
    public boolean hasBackBlock(Material material){
        return backBlocks.contains(material);
    }

    private static Material getMaterial(String materialName){
        Material material = Material.matchMaterial(materialName);

        if(material == null)
            throw new IllegalStateException("\"" + materialName + "\" is not a material!");

        return material;
    }

    private void readData(BufferedReader reader) throws IOException {
        double x, y, z;
        float yaw, pitch;
        String worldName;
        int numberOfBackBlocks;
        String[] backBlockNamesSet;
        String categoryString;

        x = Double.parseDouble(reader.readLine());
        y = Double.parseDouble(reader.readLine());
        z = Double.parseDouble(reader.readLine());
        yaw = Float.parseFloat(reader.readLine());
        pitch = Float.parseFloat(reader.readLine());
        worldName = reader.readLine();
        name = reader.readLine();

        String expLine = reader.readLine();
        if(expLine.startsWith("exp:")) { // exp line exists, next line is category
            finishExpSource = FinishExpSource.valueOf(reader.readLine());
            categoryString = reader.readLine();
            exp = Long.parseLong(expLine.substring("exp:".length()));
        }
        else { // exp line doesn't exist, this line is category
            exp = 0;
            categoryString = expLine;
        }

        identifier = Integer.parseInt(reader.readLine());
        description = reader.readLine();
        numberOfBackBlocks = Integer.parseInt(reader.readLine());

        backBlockNamesSet = new String[numberOfBackBlocks];
        backBlocks.clear();
        for (int i = 0; i < numberOfBackBlocks; i++){
            backBlockNamesSet[i] = reader.readLine();
            backBlocks.add(getMaterial(backBlockNamesSet[i]));
        }

        World world = Bukkit.getWorld(worldName);

        String line = reader.readLine();
        checkpoints.clear();
        if (line != null) {
            int checkpointsNumber = Integer.parseInt(line);
            for (int i = 0; i < checkpointsNumber; i++) {
                String currentLine = reader.readLine();
                List<String> convertedLine = Stream.of(currentLine.split(",", -1))
                        .collect(Collectors.toList());
                double xCP = Double.parseDouble(convertedLine.get(0));
                double yCP = Double.parseDouble(convertedLine.get(1));
                double zCP = Double.parseDouble(convertedLine.get(2));
                Location checkpointLocation = new Location(world, xCP, yCP, zCP);
                checkpoints.add(checkpointLocation);
            }
        }


        location = new Location(world, x, y, z, yaw, pitch);
        category = ParkourCategory.valueOf(categoryString);
    }

    public String getWorldNameFromFile(String directory){
        if (!new File(directory).exists()){
            getLogger().info("Missing file: " + directory);
            return null;
        }
        try{
            FileReader fileReader = new FileReader(directory);
            BufferedReader reader = new BufferedReader(fileReader);

            String worldName;


            Double.parseDouble(reader.readLine()); //x
            Double.parseDouble(reader.readLine()); //y
            Double.parseDouble(reader.readLine()); //z
            Float.parseFloat(reader.readLine()); //yaw
            Float.parseFloat(reader.readLine()); //pitch
            worldName = reader.readLine();

            reader.close();
            fileReader.close();

            return worldName;

        } catch(IOException a){
            System.out.println("ladowanie informacji o swiecie nie powiodlo sie");
            System.out.println(Arrays.toString(a.getStackTrace()));
        }
        return null;
    }

    private void writeData(BufferedWriter writer) throws IOException {
        writer.write(location.getX()+"\n");
        writer.write(location.getY()+"\n");
        writer.write(location.getZ()+"\n");
        writer.write(location.getYaw()+"\n");
        writer.write(location.getPitch()+"\n");
        if(location.getWorld() != null)
            writer.write(Objects.requireNonNull(location.getWorld()).getName()+"\n");
        else
            writer.write("nullPoiter\n");
        writer.write(name+"\n");
        writer.write("exp:"+exp+"\n");
        writer.write(finishExpSource.name()+"\n");
        writer.write(category.toString()+"\n");
        writer.write(identifier+"\n");
        writer.write(description+"\n");
        writer.write(backBlocks.size()+"\n");
        for (Material backBlock : backBlocks){
            writer.write(backBlock.name()+"\n");
        }
        if (!checkpoints.isEmpty()) {
            writer.write(checkpoints.size()+"\n");
            for(Location location : checkpoints){
                writer.write(location.getBlockX()+","+location.getBlockY()+","+location.getBlockZ()+"\n");
            }
        }
    }
    public synchronized void loadTopList(){
        if(isTopListLoaded) return; // method safe if is loaded
        isTopListLoaded = true;
        File topListFile = new File(folderName+ topList.fileNameInsideFolder);
        if (topListFile.exists()) {
            try {
                topList.loadTopListString(folderName);
            } catch (IOException | CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadParkour(String directory, boolean loadTopList) { // uwaga, ponowne ładowanie topki powoduje jej dublowanie
        //Bukkit.getLogger().info(directory);
        if (!new File(directory + dataFileNameInsideFolder).exists()){
            getLogger().info("Missing file: " + directory + dataFileNameInsideFolder);
            return;
        }
        try{
            FileReader fileReader = new FileReader(directory + dataFileNameInsideFolder);
            BufferedReader reader = new BufferedReader(fileReader);
            readData(reader);
            reader.close();
            fileReader.close();

//            getLogger().info("wczytano parkour:\n      pkName = " + name + "\n      location = " + location.toString()
//                    + "\n      backBlocks = " + backBlocks);

            if(loadTopList) {
                File topListFile = new File(directory + topList.fileNameInsideFolder);
                if (topListFile.exists()) {
//                getLogger().info("Wczytywanie topek...");
                    topList.loadTopListString(directory);
                }
            }
        } catch(IOException a){
            System.out.println("E KURDE COS JEST NIE TAK DXD");
            System.out.println(Arrays.toString(a.getStackTrace()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    public void saveParkour(String directory) {
        if (new File(directory).delete()){
            getLogger().info("Nadpisywanie parkoura: " + directory);
            FileCreator.createFile(directory);
        }
        try{
            FileWriter fileWriter = new FileWriter(directory);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writeData(writer);
            writer.close();
            fileWriter.close();

            getLogger().info("zapisano parkour:\n      pkName = " + name + "\n      location = " + location.toString()
                    + "\n      backBlocks = " + backBlocks);
        } catch(IOException r){
            System.out.println("E KURDE COS JEST NIE TAK DXD");
            System.out.println(Arrays.toString(r.getStackTrace()));
        }
    }
    private int generateID(){
        ParkourSet parkourSet = ParkourPlugin.parkourSet;
        return parkourSet.getOptimalIdentifierToAdd(parkourSet.getAllMapsOfCategory(this.category));
    }

    public boolean didPlayerFinishParkour(Player player){
        List<TopLine> topList = getTopListObject().getTopList(false,true,true);
        for (TopLine topLine : topList){
            if (topLine.player.getUniqueId().equals(player.getUniqueId())) return true;
        }
        return false;
    }
    public List<Location> getCheckpoints(){
        return checkpoints;
    }
}
