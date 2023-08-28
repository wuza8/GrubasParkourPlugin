package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.UUIDList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.sqrt;

public class UserManager {

    //ingame table will be multiplied by multiplier
    public static float multiplier = 0.5f; // it should take 1h for skilled player to get next lvl if this value is 1.0f
    public static int[] xpTable = {
            //LVL 1-10
            0, //1
            3600,
            7200,
            10800,
            14400,
            18000,
            21600,
            25200,
            28800,
            32400, // 10
            39600,
            46800,
            54000,
            61200,
            68400,
            75600,
            82800,
            90000,
            97200,
            104400, // 20
            115200,
            126000,
            136800,
            147600,
            158400,
            169200,
            180000,
            190800,
            201600,
            212400 //30
    };

    public static String[] tiltes = {
            "Nowy w miescie",
            "Azbest na budowie",
            "Pijany brygadzista",
            "Zabkowy wlasciciel",
            "Pierwszy domek",
            "Zal zlotowy",
            "Okulary Versacziego",
            "Pierwszy Roli",
            "Pieniadzoprad",
            "Pulsonic",
            "Potezny Portfel",
            "Krol Kuponow",
            "Rustler",
            "Plynący w Gotowce",
            "Platynowy Papier",
            "Fortuna w Budowie",
            "Inwestycja w Budowie",
            "Srebrny Sztok",
            "Nowy Roli",
            "Pierwszy Sklep",
            "Zloty Szef",
            "Monopolowy Mistrz",
            "Ryzykant Rynkowy",
            "Predator Alkoholowy",
            "Marzowy Mistrz",
            "Banknotowy Baron",
            "Inwestycyjny Ideal",
            "Pieniadzowy Profesor",
            "Gruby zwierz",
            "Zawal miliardera"
    };

    public static HashMap<String, User> playerUserHashMap = new HashMap<>();
    public static boolean containsUser(String name){
        for (String userName : playerUserHashMap.keySet()){
            if(userName.equals(name))
                return true;
        }
        return false;
    }
    public static User getUserByName(String playerName){
        return playerUserHashMap.get(playerName);
    }
    private static void addUserToPlayerHashmap(String player, User user){
        playerUserHashMap.put(player,user);
    }
//
//    public static User getUserByName(String name){
//        for (User user : users)
//            if (user.getNick().equals(name))
//                return user;
//        return null;
//    }

    public static User createUser(String playerNick){
        User user = new User(playerNick);
        playerUserHashMap.put(playerNick,user);
        LevelFile levelFile = LevelFile.getInstance();

        if(levelFile.getData().getConfigurationSection("Users." + playerNick) == null){
            levelFile.getData().createSection("Users." + playerNick);
            levelFile.getData().set("Users." + playerNick + ".Exp", user.getExp());
            levelFile.saveData();
        }
        addUserToPlayerHashmap(playerNick, user);
        return user;
    }
    public static void resetAllUsers(){
        playerUserHashMap = new HashMap<>();
        LevelFile.getInstance().deleteLevelFile();
        LevelFile.getInstance().setup(ParkourPlugin.getInstance());
    }

    public static int getLevelOfExp(long exp) {
        //Stare expowanie:
        //return (int)Math.floor((16 + sqrt(256 + 64 * exp))/32);
        int lvl = 0;

        for(int z : xpTable){
            if(z*multiplier <= exp){
                lvl++;
            }
        }

        return lvl;
    }
}
