package aybici.parkourplugin.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    public static String fixColor(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String hex(String string){
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(string);
        while(matcher.find()){
            String hexCode = string.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("#", "x");
            char[] ch = replaceSharp.toCharArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for(char c : ch){
                stringBuilder.append("&" + c);
            }
            string = string.replace(hexCode, stringBuilder.toString());
            matcher = pattern.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
