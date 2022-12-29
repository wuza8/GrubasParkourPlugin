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
    public static List<User> users = new ArrayList<>();
    public static HashMap<String, User> playerUserHashMap = new HashMap<>();
    public static boolean containsUser(String name){
        for (User user : users){
            if(user.getNick().equals(name))
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
        users.add(user);
        LevelFile levelFile = LevelFile.getInstance();

        if(levelFile.getData().getConfigurationSection("Users." + playerNick) == null){
            levelFile.getData().createSection("Users." + playerNick);
            levelFile.getData().set("Users." + playerNick + ".Exp", user.getExp());
            getUserByName(playerNick).setLevel(1);
            levelFile.getData().set("Users." + playerNick + ".Level", user.getLevel());
            levelFile.saveData();
        }
        addUserToPlayerHashmap(playerNick, user);
        return user;
    }
    public static void resetAllUsers(){
        users = new ArrayList<>();
        LevelFile.getInstance().deleteLevelFile();
        LevelFile.getInstance().setup(ParkourPlugin.getInstance());
    }
    public static void fixLevel(User user){
        user.setLevel(getLevelOfExp(user.getExp()));
    }
    public static int getLevelOfExp(long exp){
        return (int)Math.floor((16 + sqrt(256 + 64 * exp))/32);
    }
}
