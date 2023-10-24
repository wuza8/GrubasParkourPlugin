package aybici.parkourplugin.chests;

import aybici.parkourplugin.ParkourPlugin;
import aybici.parkourplugin.users.User;
import aybici.parkourplugin.users.UserManager;
import aybici.parkourplugin.utils.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Random;

public class ChestManager {

    public static HashMap<Player, Long> cooldown = new HashMap<>();

    public static void openChest(Player player){
        User user = UserManager.getUserByName(player.getName());

        int chance = new Random().nextInt(10);

        Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botwiera skrzynię!"));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatUtil.fixColor("&aOtwieranie&b")));
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);

        ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatUtil.fixColor("&aOtwieranie&b.")));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatUtil.fixColor("&aOtwieranie&b.")));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatUtil.fixColor("&aOtwieranie&b.")));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                    ParkourPlugin.getInstance().getServer().getScheduler().runTaskLater(ParkourPlugin.getInstance(), () -> {

                        switch(chance){
                            case 0:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a100 XP"));
                                user.addExp(100);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a100 XP"));
                                break;

                            case 1:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a200 XP"));
                                user.addExp(200);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a200 XP"));
                                break;

                            case 2:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a300 XP"));
                                user.addExp(300);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a300 XP"));
                                break;

                            case 3:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &7zwykła"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a400 XP"));
                                user.addExp(400);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a400 XP"));
                                break;

                            case 4:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a500 XP"));
                                user.addExp(500);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a500 XP"));
                                break;

                            case 5:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a600 XP"));
                                user.addExp(600);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a600 XP"));
                                break;

                            case 6:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a700 XP"));
                                user.addExp(700);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a700 XP"));
                                break;

                            case 7:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a800 XP"));
                                user.addExp(800);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a800 XP"));
                                break;

                            case 8:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a900 XP"));
                                user.addExp(900);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a900 XP"));
                                break;

                            case 9:
                                player.sendTitle("", ChatUtil.fixColor("&bNagroda: &9rzadka"), 30, 30, 30);
                                player.sendMessage(ChatUtil.fixColor("&b>&a> &aNagroda: &a1000 XP"));
                                user.addExp(1000);
                                user.saveUser();
                                player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
                                Bukkit.broadcastMessage(ChatUtil.fixColor("&b>&a> &bGracz &a" + player.getName() + " &botrzymał: &a1000 XP"));
                                break;
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
        player.sendMessage(ChatUtil.fixColor("&bZdobyłeś klucz!"));

        Firework f = (Firework) player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect
                .builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.STAR)
                .withColor(Color.AQUA)
                .withFade(Color.GREEN)
                .build());
        fm.setPower(0);
        f.setFireworkMeta(fm);
        f.detonate();
    }
}

// 100-400 - 65%
// 500-1000 - 30%
// 1100-2000 - 5%