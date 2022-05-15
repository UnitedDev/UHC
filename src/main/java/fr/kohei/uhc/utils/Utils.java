package fr.kohei.uhc.utils;

import org.bukkit.entity.Player;

public class Utils {

    public static void resetPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

}
