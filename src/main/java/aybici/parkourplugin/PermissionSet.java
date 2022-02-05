package aybici.parkourplugin;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionSet {
    public Permission apkPermission = new Permission("apkPermission", PermissionDefault.OP);
    public Permission allParkoursPermission = new Permission("allParkoursPermission", PermissionDefault.OP);
    public Permission buildPermission = new Permission("buildPermission", PermissionDefault.OP);
    public Permission itemsPermission = new Permission("itemsPermission", PermissionDefault.OP);
    public Permission pkQuitPermission = new Permission("pkquitPermission", PermissionDefault.OP);
    public Permission setLobbyPermission = new Permission("setlobbyPermission", PermissionDefault.OP);
    public Permission tpAllToLobbyPermission = new Permission("tpAllToLobbyPermission", PermissionDefault.OP);
    public Permission goingAwayFromLobby = new Permission("goingAwayFromLobbyPermission", PermissionDefault.OP);
}
