package fr.kohei.uhc.task;

import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class CycleTask extends BukkitRunnable {

    private final GameManager gameManager;
    private boolean day;

    public CycleTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {

        new BukkitRunnable() {
            @Override
            public void run() {
                setDay(true);
                Bukkit.broadcastMessage(ChatUtil.translate("&e&l☀ &eLe jour se lève..."));
                Module module = UHC.getModuleManager().getModule();
                module.onDay();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> UPlayer.get(player).getRole() != null)
                        .forEach(player -> UPlayer.get(player).getRole().onDay(player));
            }
        }.runTaskTimer(UHC.getPlugin(), 0, gameManager.getGameConfiguration().getCycle() * 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                setDay(false);
                Bukkit.broadcastMessage(ChatUtil.translate("&9&l☾ &9La nuit tombe..."));
                Module module = UHC.getModuleManager().getModule();
                module.onNight();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> UPlayer.get(player).getRole() != null)
                        .forEach(player -> UPlayer.get(player).getRole().onNight(player));
            }
        }.runTaskTimer(UHC.getPlugin(), gameManager.getGameConfiguration().getCycle(), gameManager.getGameConfiguration().getCycle() * 2L);

    }

}
