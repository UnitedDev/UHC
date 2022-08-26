package fr.uniteduhc.uhc.task;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.utils.ScoreboardTeam;
import fr.uniteduhc.utils.Title;
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
            UHC.getInstance().getGameManager().startGame();
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 5, 1);
                Title.sendTitle(player, 0, 20, 0, "&8» &cC'est parti ! &8«", "");
            });
            for (Player players1 : Bukkit.getOnlinePlayers()) {
                for (ScoreboardTeam teams : UHC.getInstance().getTeams()) {
                    ((CraftPlayer) players1).getHandle().playerConnection.sendPacket(teams.removeTeam());
                }
            }
            cancel();
            return;
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
            Title.sendTitle(player, 0, 20, 0, "&e" + timer, "");
        });
    }
}
