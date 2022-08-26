package fr.uniteduhc.uhc.task;

import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.module.Module;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameManager;
import fr.uniteduhc.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class CycleTask {

    private final GameManager gameManager;
    private boolean day;

    public CycleTask(GameManager gameManager) {
        this.gameManager = gameManager;

        this.run();
    }

    public void run() {

        new BukkitRunnable() {
            @Override
            public void run() {
                setDay(true);
                Bukkit.broadcastMessage(ChatUtil.translate("&e&l☀ &eC'est le début du jour..."));
                Module module = UHC.getInstance().getModuleManager().getModule();
                module.onDay();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> UPlayer.get(player).getRole() != null)
                        .forEach(player -> UPlayer.get(player).getRole().onDay(player));
            }
        }.runTaskTimer(UHC.getInstance(), 0, gameManager.getGameConfiguration().getCycle() * 40L);

        new BukkitRunnable() {
            @Override
            public void run() {
                setDay(false);
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(ChatUtil.translate("&9&l☾ &9La nuit tombe..."));
                Bukkit.broadcastMessage(" ");
                Module module = UHC.getInstance().getModuleManager().getModule();
                module.onNight();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> UPlayer.get(player).getRole() != null)
                        .forEach(player -> UPlayer.get(player).getRole().onNight(player));
            }
        }.runTaskTimer(UHC.getInstance(), gameManager.getGameConfiguration().getCycle() * 20L, gameManager.getGameConfiguration().getCycle() * 40L);

    }

}
