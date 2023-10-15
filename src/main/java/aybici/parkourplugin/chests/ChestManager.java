package aybici.parkourplugin.chests;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import jdk.tools.jmod.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class ChestManager {

    public static void openChest(Player player){
        User user = UserManager.getUserByName(player.getName());

        if(player == null) return;

        int chance = new Random().nextInt(100);
        Random randomBadExp = new Random();
        Random randomRareExp = new Random();
        Random randomLegendExp = new Random();

        int badExp = randomBadExp.nextInt(4);
        int rareExp = randomRareExp.nextInt(6);
        int legendExp = randomLegendExp.nextInt(13);

        int actualKeys = user.getKeys();
        user.removeKeys(1);

        user.saveUser();

        player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b"));
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);

        ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
            player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b."));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b.."));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b..."));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                    ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {

                        if(chance < 99) {
                            if(badExp == 0){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a100 XP"));
                                user.addExp(100);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 1){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a200 XP"));
                                user.addExp(200);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 2){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a300 XP"));
                                user.addExp(300);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 3){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a400 XP"));
                                user.addExp(400);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                        }

                        if(chance < 30){
                            if(rareExp == 0){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a500 XP"));
                                user.addExp(500);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(rareExp == 1){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a600 XP"));
                                user.addExp(600);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(rareExp == 2){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a700 XP"));
                                user.addExp(700);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(rareExp == 3){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a800 XP"));
                                user.addExp(800);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(rareExp == 4){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a900 XP"));
                                user.addExp(900);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(rareExp == 5){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1000 XP"));
                                user.addExp(1000);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                        }

                        if(chance < 5){
                            if(legendExp == 0){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1100 XP"));
                                user.addExp(1100);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 1){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1200 XP"));
                                user.addExp(1200);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 2){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1200 XP"));
                                user.addExp(1200);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 3){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1300 XP"));
                                user.addExp(1300);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 4){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1400 XP"));
                                user.addExp(1400);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 5){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1500 XP"));
                                user.addExp(1500);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 6){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1600 XP"));
                                user.addExp(1100);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 7){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1700 XP"));
                                user.addExp(1700);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 8){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1600 XP"));
                                user.addExp(1600);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 9){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1700 XP"));
                                user.addExp(1700);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 10){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1800 XP"));
                                user.addExp(1800);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 11){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1900 XP"));
                                user.addExp(1900);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(legendExp == 12){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a2000 XP"));
                                user.addExp(2000);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                        }
                    }, 20L);
                }, 20L);
            }, 20L);
        }, 20L);
    }

    public static void addKeyAfterPlayerFinishedParkour(Player player, int keys){
        User user = UserManager.getUserByName(player.getName());
        if(player == null) return;
        user.addKeys(1);
        user.saveUser();
    }
}

// 100-400 - 65%
// 500-1000 - 30%
// 1100-2000 - 5%