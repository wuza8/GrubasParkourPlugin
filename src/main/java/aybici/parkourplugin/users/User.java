package aybici.parkourplugin.users;

public class User {
    private String nick;
    private int level;
    private long exp;

    public User(final String name){
        this.nick = name;
    }

    public long getNeedExp(){
        final int multiplier = 32;
        int i = 1;
        long need = 0;
        long needexp = 0;
        while(i <= this.level){
            need = needexp;
            needexp = (long) i * multiplier + need;
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

    public long getExp(){
        return this.exp;
    }

    public void setExp(long exp){
        this.exp = exp;
    }
    public void addExp(long exp){
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
