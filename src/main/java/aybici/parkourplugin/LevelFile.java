package aybici.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

// Ta klasa została skopiowana z odcinka technic cupa!
public class LevelFile {

    static LevelFile instance;
    Plugin p;
    FileConfiguration data;
    public static File rfile;

    static {
        instance = new LevelFile();
    }

    public static LevelFile getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        if(!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        File path = new File(p.getDataFolder() + File.separator + "/data");
        rfile = new File(path, String.valueOf(File.separator + "level.yml"));
        if(!rfile.exists()) {
            try {
                path.mkdirs();
                rfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Nie udalo sie stworzyc pliku o nazwie level.yml");
            }
        }
        data = YamlConfiguration.loadConfiguration(rfile);
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveData() {
        try{
            data.save(rfile);
        } catch(IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Nie udalo sie zapisac pliku o nazwie level.yml");
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(rfile);
    }
}
