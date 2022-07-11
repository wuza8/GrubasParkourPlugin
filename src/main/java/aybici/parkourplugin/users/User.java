package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nick;
    private int level;
    private int exp;
    public static List<User> users = new ArrayList<>();

    public User(final String name){
        this.nick = name;
    }

    public int getNeedExp(){
        final int multipler = 32;
        int i = 1;
        int need = 0;
        int needexp = 0;
        while(i <= this.level){
            need = needexp;
            needexp = i * multipler + need;
            i++;
        }
        return needexp - this.exp;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getExp(){
        return this.exp;
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public String getNick(){
        return this.nick;
    }

    public static boolean containsUser(String name){
        for (User user : users){
            if(user.getNick().equals(name)) return true;
        }
        return false;
    }

    public static User getUserByName(String name){
        for(User user : users){
            if(user.getNick().equals(name)) return user;
        }
        return null;
    }

    public static User createUser(String playerNick){
        User user = new User(playerNick);
        users.add(user);
        LevelFile levelFile = LevelFile.getInstance();

        if(levelFile.getData().getConfigurationSection("Users." + playerNick ) == null){
            levelFile.getData().createSection("Users." + playerNick);
            levelFile.getData().set("Users." + playerNick + ".Exp", user.getExp());
            user.setLevel(1);
            levelFile.getData().set("Users." + playerNick + ".Level", user.getLevel());
            levelFile.saveData();
        }
        return user;
    }
}
