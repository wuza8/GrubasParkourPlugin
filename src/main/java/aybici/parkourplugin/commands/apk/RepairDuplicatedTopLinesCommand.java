package aybici.parkourplugin.commands.apk;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.parkours.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// usuwa tylko powielenie topek w plikach, należy uruchomić
// serwer ponownie, zeby je załadowac poprwanie
public class RepairDuplicatedTopLinesCommand extends AdminParkourCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!SenderHasPermission(sender, ParkourPlugin.permissionSet.configureParkourPermission)) return true;

        if(args.length != 0){
            String parkourName = args[0];
            Parkour parkour = ParkourPlugin.parkourSet.getParkour(parkourName);
            try {
                repairTopList(parkour);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else{
            for (Parkour parkour : ParkourPlugin.parkourSet.getParkours()){
                try{
                    repairTopList(parkour);
                } catch(Exception e){
                    Bukkit.getLogger().info(e.getMessage());
                }

            }
        }
        sender.sendMessage("Proces przebiegł pomyślnie, uruchom ponownie serwer, aby załadować topki ponownie!");


        return true;
    }


    private void repairTopList(Parkour parkour) throws IOException {
        String topListLocation = parkour.folderName + parkour.getTopListObject().fileNameInsideFolder;
        String writeLocation = parkour.folderName + File.separator + "naprawioneTopki.txt";
        String afterRename = parkour.folderName + File.separator + "zdublowaneTopki.txt";

        File duplicatedTopFile = new File(topListLocation);

        if(duplicatedTopFile.exists()) {

            List<SimpleTopline> topList = new ArrayList<>();

            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(topListLocation));
                boolean newLineExists = true;
                while(newLineExists){
                    String line = bufferedReader.readLine();
                    if(line != null && !line.equals("")) {
                        List<String> convertedLine = Stream.of(line.split(",", -1))
                                .collect(Collectors.toList());
                        topList.add(new SimpleTopline(Short.parseShort(convertedLine.get(0)),
                                                        Long.parseLong(convertedLine.get(1)), line));
                    }
                    else {
                        newLineExists = false;
                        bufferedReader.close();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<SimpleTopline> nonRepeated = new ArrayList<>();

            boolean contains = false;
            for(SimpleTopline topline : topList){
                for(SimpleTopline topline1 : nonRepeated){
                    if(topline1.date == topline.date ) {
                        if(topline1.playerID == topline.playerID){
                            contains = true;
                            break;
                        }else{
                            Bukkit.getLogger().info("żadki przypadek, 2 topki w tym samym momencie\n" + parkour.getName()
                            + "\n" + topline.line + "\n"
                            + topline1.line);
                        }

                    }
                }
                if(!contains)
                    nonRepeated.add(topline);
                contains = false;
            }

            List<SimpleTopline> topListSorted  = sort(nonRepeated);

            File repairedFile = new File(writeLocation);
            BufferedWriter writer = new BufferedWriter(new FileWriter(repairedFile));
            for(SimpleTopline topline : topListSorted ){
                writer.write(topline.line + "\n");
            }

            writer.close();

            if(duplicatedTopFile.renameTo(new File(afterRename))) {

                if(repairedFile.renameTo(new File(topListLocation)))
                    new File(afterRename).delete();
                else
                    Bukkit.getLogger().info("nie udało sie zmienić nazwy pliku: \n" + repairedFile.getAbsolutePath());

            }
            else Bukkit.getLogger().info("nie udało sie zmienić nazwy pliku: \n" + duplicatedTopFile.getAbsolutePath());



        } else Bukkit.getLogger().info("Brak pliku topki: " + topListLocation);

    }
    private static List<SimpleTopline> sort(List<SimpleTopline> topList){
        topList.sort(Comparator.comparing(o -> o.date));
        return topList;
    }
    private class SimpleTopline {
        public String line;
        public long date;
        public short playerID;
        public SimpleTopline(short playerID, long date, String line){
            this.date = date;
            this.line = line;
            this.playerID = playerID;
        }
    }

}

