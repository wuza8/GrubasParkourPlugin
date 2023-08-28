package aybici.parkourplugin.users;

public class User {
    private String nick;
    private long exp;
    private boolean isCheater;

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
    public void saveUser(){
        UserFile.levelFile.getData().set("Users." + nick + ".Exp", getExp());
        UserFile.levelFile.getData().set("Users." + nick + ".Cheater", isCheater);
        UserFile.levelFile.saveData();
    }

}
