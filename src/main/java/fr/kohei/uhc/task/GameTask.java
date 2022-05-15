package fr.kohei.uhc.task;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final GameManager gameManager;

    public GameTask(GameManager gameManager) {
        this.gameManager = gameManager;

        this.runTaskTimer(UHC.getPlugin(), 20, 20);
    }

    @Override
    public void run() {
        gameManager.setDuration(gameManager.getDuration() + 1);
    }
}
