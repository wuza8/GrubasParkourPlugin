package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nick;
    private int level;
    private int exp;

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

}
