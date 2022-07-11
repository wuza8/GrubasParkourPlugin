package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;

import java.util.List;

public class User {
    private String nick;
    private int level;
    private int exp;
    public static List<User> users;

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

    public static User createUser(String playerNick){
        users.add(new User(playerNick));
        LevelFile levelFile = LevelFile.getInstance();
        if(levelFile.getData().getConfigurationSection("Users." + playerNick ) == null){
            levelFile.getData().createSection("Users." + new User(playerNick).getNick());
            levelFile.getData().set("Users." + new User(playerNick).getNick() + ".Exp", Integer.valueOf(new User(playerNick).getExp()));
            new User(playerNick).setLevel(1);
            levelFile.getData().set("Users." + new User(playerNick).getNick() + ".Level", Integer.valueOf(new User(playerNick).getLevel()));
            levelFile.saveData();
        }
        return new User(playerNick);
    }
}
