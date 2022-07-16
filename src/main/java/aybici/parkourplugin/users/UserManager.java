package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;
import aybici.parkourplugin.ParkourPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<User> users = new ArrayList<>();
    public static boolean containsUser(String name){
        for (User user : users){
            if(user.getNick().equals(name))
                return true;
        }
        return false;
    }

    public static User getUserByName(String name){
        for (User user : users)
            if (user.getNick().equals(name))
                return user;
        return null;
    }

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
        return user;
    }
    public static void resetAllUsers(){
        users = new ArrayList<>();
        LevelFile.getInstance().deleteLevelFile();
        LevelFile.getInstance().setup(ParkourPlugin.getInstance());
    }
}