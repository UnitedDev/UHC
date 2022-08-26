package fr.uniteduhc.uhc.task;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.cache.rank.Rank;
import fr.uniteduhc.mumble.api.LinkAPI;
import fr.uniteduhc.mumble.api.mumble.IUser;
import fr.uniteduhc.mumble.api.mumble.MumbleState;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.utils.ScoreboardTeam;
import fr.uniteduhc.uhc.game.GameState;
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
        if (UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) return;
        for (Player players1 : Bukkit.getOnlinePlayers()) {
            for (Player players2 : Bukkit.getOnlinePlayers()) {

                Rank rank = BukkitAPI.getCommonAPI().getProfile(players2.getUniqueId()).getRank();

                ScoreboardTeam team;
                if (LinkAPI.getApi().isEnabled()) {
                    IUser user = LinkAPI.getApi().getMumbleManager().getUserFromName(players2.getName());
                    char character = UHC.RANKS_ALPHABET.get(rank.token());
                    if (user == null) {
                        team = UHC.getInstance().getScoreboardTeam(character + "3");
                    } else if (LinkAPI.getApi().getMumbleManager().getStateOf(user.getName()) == MumbleState.LINK) {
                        team = UHC.getInstance().getScoreboardTeam(character + "1");
                    } else {
                        team = UHC.getInstance().getScoreboardTeam(character + "2");
                    }
                } else {
                    char character = UHC.RANKS_ALPHABET.get(rank.token());
                    team = UHC.getInstance().getScoreboardTeam(character + "4");
                }
                ((CraftPlayer) players1).getHandle().playerConnection.sendPacket(team.addOrRemovePlayer(3, players2.getName()));
            }

        }

    }

}
