package aybici.parkourplugin.users;

import org.bukkit.ChatColor;

public class User {
    private String nick;
    private int level;
    private int exp;

    public User(final String name){
        this.nick = name;
    }

    public int getNeedExp(){
        final int multiplier = 32;
        int i = 1;
        int need = 0;
        int needexp = 0;
        while(i <= this.level){
            need = needexp;
            needexp = i * multiplier + need;
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
    public void addExp(int exp){
        this.exp = this.exp + exp;
    }

    public String getNick(){
        return this.nick;
    }
    public void saveUser(){
        UserFile.levelFile.getData().set("Users." + nick + ".Exp", getExp());
        UserFile.levelFile.getData().set("Users." + nick + ".Level", getLevel());
        UserFile.levelFile.saveData();
    }

}
