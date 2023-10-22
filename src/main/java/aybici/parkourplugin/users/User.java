package aybici.parkourplugin.users;

public class User {
    private String nick;
    private long exp;
    private boolean isCheater;
    private int keys;
    private int worldRecords;

    public User(final String name){
        this.nick = name;
    }

    public long getNeedExp(){
        if(getLevel() == 30) return 0;
        long needexp = (long) (UserManager.xpTable[getLevel()] * UserManager.multiplier);
        return needexp - this.exp;
    }

    public int getLevel(){
        return UserManager.getLevelOfExp(getExp());
    }

    public void setCheater(boolean value){
        this.isCheater = value;
    }
    public boolean isCheater(){return isCheater;}

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

    public int getKeys(){
        return keys;
    }

    public void addKeys(int keys){
        this.keys += keys;
    }

    public void removeKeys(int keys){
        this.keys -= keys;
    }

    public int getWorldRecordsAmount(){
        return worldRecords;
    }

    public void setWorldRecordsAmount(int worldRecords){
        this.worldRecords = worldRecords;
    }

    public void addWorldRecordsAmount(int worldRecords){
        this.worldRecords += worldRecords;
    }

    public void removeWorldRecordsAmount(int worldRecords){
        this.worldRecords -= worldRecords;
    }

    public void saveUser(){
        UserFile.levelFile.getData().set("Users." + nick + ".Exp", getExp());
        UserFile.levelFile.getData().set("Users." + nick + ".Cheater", isCheater);
        UserFile.levelFile.getData().set("Users." + nick + ".Keys", getKeys());
        UserFile.levelFile.getData().set("Users." + nick + ".WorldRecords", getWorldRecordsAmount());
        UserFile.levelFile.saveData();
    }

}
