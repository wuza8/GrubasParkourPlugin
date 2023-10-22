package aybici.parkourplugin.listeners;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.chests.ChestManager;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class ChestListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Action action = event.getAction();

        if(action != Action.RIGHT_CLICK_BLOCK)
            return;

        Material blockType = event.getClickedBlock().getType();

        if(blockType != Material.CHEST)
            return;

        int x = event.getClickedBlock().getX();
        int y = event.getClickedBlock().getY();
        int z= event.getClickedBlock().getZ();

        if(!ParkourPlugin.getInstance().chestLocationConfig.getBoolean("chests." + x + "," + y + "," + z))
            return;

        Player player = event.getPlayer();
        User user = UserManager.getUserByName(player.getName());

        if(user.getKeys() < 1){
            player.sendMessage(ChatUtil.fixColor("&b>&a> &cNie posiadasz kluczy!"));
            event.setCancelled(true);
            return;
        }

        user.removeKeys(1);
        user.saveUser();

        event.setCancelled(true);

        int chance = new Random().nextInt(100);
        Random randomBadExp = new Random();
        Random randomRareExp = new Random();
        Random randomLegendExp = new Random();

        int badExp = randomBadExp.nextInt(4);
        int legendExp = randomLegendExp.nextInt(13);

        player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b"), 10, 20, 10);
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);

        ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
            player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b."), 10, 20, 10);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b.."), 10, 20, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    player.sendTitle("", ChatUtil.fixColor("&aOtwieranie&b..."), 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                    ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {

                        if(chance < 99) {
                            if(badExp == 0){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a100 XP"));
                                user.addExp(100);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 1){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a200 XP"));
                                user.addExp(200);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 2){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a300 XP"));
                                user.addExp(300);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                            if(badExp == 3){
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a400 XP"));
                                user.addExp(400);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                            }
                        }

                        if(chance < 30){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a500 XP"));
                            user.addExp(500);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 29){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a600 XP"));
                            user.addExp(600);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 28){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a700 XP"));
                            user.addExp(700);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 27){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a800 XP"));
                            user.addExp(800);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 26){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a900 XP"));
                            user.addExp(900);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 25){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1000 XP"));
                            user.addExp(1000);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 5){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1600 XP"));
                            user.addExp(1600);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 4){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1700 XP"));
                            user.addExp(1700);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 3){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1800 XP"));
                            user.addExp(1800);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 2){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1900 XP"));
                            user.addExp(1900);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }

                        if(chance < 1){
                            player.sendTitle("", ChatUtil.fixColor("&bNagroda: &6legendarna"));
                            player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a2000 XP"));
                            user.addExp(2000);
                            user.saveUser();
                            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                        }
                    }, 20L);
                }, 20L);
            }, 20L);
        }, 20L);
    }
}
