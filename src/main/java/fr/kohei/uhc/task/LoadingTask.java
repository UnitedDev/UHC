package fr.kohei.uhc.task;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.frame.ScoreboardTeam;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadingTask extends BukkitRunnable {

    private int timer;

    public LoadingTask() {
        timer = 10;
    }

    @Override
    public void run() {
        timer--;
        if(timer == 0) {
            UHC.getGameManager().startGame();
            cancel();
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 5, 1);
                Title.sendTitle(player, 0, 20, 0, "&8» &cC'est parti ! &8«", "");
            });
            for (Player players1 : Bukkit.getOnlinePlayers()) {
                for (Player players2 : Bukkit.getOnlinePlayers()) {
                    ScoreboardTeam team = UHC.getScoreboardTeam("aa");

                    ((CraftPlayer) players1).getHandle().playerConnection.sendPacket(team.addOrRemovePlayer(3, players2.getName()));
                }
            }
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
            Title.sendTitle(player, 0, 20, 0, "&e" + timer, "");
        });
    }
}
