package aybici.parkourplugin.users;

import aybici.parkourplugin.LevelFile;

public class UserFile {

    public static LevelFile levelFile = LevelFile.getInstance();

    public static void loadUsers(){
        if(levelFile.getData().getConfigurationSection("Users") == null)
            return;
        for(String string : levelFile.getData().getConfigurationSection("Users").getKeys(false)){
            if(levelFile.getData().getConfigurationSection("Users") == null)
                return;
            User user = UserManager.createUser(string);
            user.setExp(levelFile.getData().getInt("Users." + string + ".Exp"));
            user.setLevel(UserManager.getLevelOfExp(user.getExp()));
            user.setCheater(levelFile.getData().getBoolean("Users." + string + ".Cheater"));
            user.setKeys(levelFile.getData().getInt("Users." + string + ".Keys"));
            user.setWorldRecordsAmount(levelFile.getData().getInt("Users." + string + ".WorldRecords"));
        }
    }
}
