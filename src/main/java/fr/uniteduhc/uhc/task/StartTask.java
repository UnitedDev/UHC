package fr.uniteduhc.uhc.task;

import fr.uniteduhc.uhc.game.GameManager;
import fr.uniteduhc.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTask extends BukkitRunnable {

    private int timer = 11;
    private final GameManager gameManager;

    public StartTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {

        timer--;
        if (timer == 10 || timer == 5 || timer == 4)
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 0, 20, 0, "&c" + timer, ""));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setLevel(timer);
        }

        if (timer == 10 || timer < 6) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1));
        }

        if (timer == 3) {
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 0, 20, 0, "&c" + timer, "&aPréparez vous"));
        }
        if (timer == 2) {
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 0, 20, 0, "&c" + timer, "&eA vos marques"));
        }
        if (timer == 1) {
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 0, 20, 0, "&c" + timer, "&6Prêt ?"));
        }

        if (timer == 0) {
            cancel();
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 0, 20, 0, "&cC'est parti", ""));
            gameManager.loadGame();
        }


    }

}