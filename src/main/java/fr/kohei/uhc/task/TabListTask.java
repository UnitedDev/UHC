package fr.kohei.uhc.task;

import fr.kohei.common.cache.Rank;
import fr.kohei.BukkitAPI;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.frame.ScoreboardTeam;
import fr.kohei.uhc.game.GameState;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListTask extends BukkitRunnable {

    private final JavaPlugin plugin;

    public TabListTask(JavaPlugin plugin) {
        this.plugin = plugin;

        this.runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void run() {
        updateTabRanks();
    }

    public void updateTabRanks() {
        if (UHC.getGameManager().getGameState() == GameState.LOBBY) {
            for (Player players1 : Bukkit.getOnlinePlayers()) {
                for (Player players2 : Bukkit.getOnlinePlayers()) {
                    Rank rank = BukkitAPI.getCommonAPI().getProfile(players2.getUniqueId()).getRank();
                    ScoreboardTeam team = UHC.getScoreboardTeam(String.valueOf(UHC.number(rank.permissionPower())));

                    ((CraftPlayer) players1).getHandle().playerConnection.sendPacket(team.addOrRemovePlayer(3, players2.getName()));
                }
            }
        }

    }

}
