package fr.uniteduhc.uhc.task;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameManager;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.module.manager.Role;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final GameManager gameManager;

    public GameTask(GameManager gameManager) {
        this.gameManager = gameManager;

        this.runTaskTimer(UHC.getInstance(), 20, 20);
    }

    @Override
    public void run() {
        gameManager.setDuration(gameManager.getDuration() + 1);

        Bukkit.getOnlinePlayers().forEach(player -> {
            Role role = UPlayer.get(player).getRole();

            if (role == null) return;

            role.onSecond(player);
        });
    }
}
