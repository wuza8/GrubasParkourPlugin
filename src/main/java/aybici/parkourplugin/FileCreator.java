package aybici.parkourplugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileCreator {
    public static void createFile(String directory){
        int last_slash = 0;
        for (int i = 0; i < directory.length(); i++){
            if (directory.toCharArray()[i] == '\\' || directory.toCharArray()[i] == '/'){
                last_slash = i;
            }
        }
        String folder = directory.substring(0,last_slash);
        try {
            Files.createDirectories(Paths.get(folder));
            File myObj = new File(directory);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
