package fr.kohei.uhc.game;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.player.UPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class EpisodeManager {

    private final GameManager gameManager;
    private int episode;

    public EpisodeManager(GameManager gameManager) {
        this.gameManager = gameManager;

        this.episode = 1;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setEpisode(getEpisode() + 1);
                UHC.getModuleManager().getModule().onEpisode();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> UPlayer.get(player).getRole() != null)
                        .forEach(player -> UPlayer.get(player).getRole().onEpisode(player));
            }
        }.runTaskTimer(UHC.getPlugin(), 20 * 60 * 20, 20 * 60 * 20);
    }

}
