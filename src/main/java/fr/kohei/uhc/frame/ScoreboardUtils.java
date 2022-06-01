package fr.kohei.uhc.frame;

import fr.kohei.uhc.UHC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can GRAYistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ScoreboardUtils {
    private final Map<UUID, ScoreboardModifier> scoreboards;
    private final ScheduledFuture glowingTask;
    private final ScheduledFuture reloadingTask;
    private int ipCharIndex;
    private int cooldown;

    public ScoreboardUtils() {
        scoreboards = new HashMap<>();
        ipCharIndex = 0;
        cooldown = 0;

        glowingTask = UHC.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            String ip = colorIpAt();
            for (ScoreboardModifier scoreboard : scoreboards.values())
                UHC.getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
        }, 80, 80, TimeUnit.MILLISECONDS);

        reloadingTask = UHC.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (ScoreboardModifier scoreboard : scoreboards.values())
                UHC.getExecutorMonoThread().execute(scoreboard::reloadData);
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void onDisable() {
        scoreboards.values().forEach(ScoreboardModifier::onLogout);
    }

    public void onLogin(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            return;
        }
        scoreboards.put(player.getUniqueId(), new ScoreboardModifier(player));
    }

    public void onLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    public void update(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).reloadData();
        }
    }

    private String colorIpAt() {
        String ip = "     kohei.fr";

        if (cooldown > 0) {
            cooldown--;
            return ChatColor.RED + ip;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (ipCharIndex > 0) {
            formattedIp.append(ip, 0, ipCharIndex - 1);
            formattedIp.append(ChatColor.GRAY).append(ip.charAt(ipCharIndex - 1));
        } else {
            formattedIp.append(ip, 0, ipCharIndex);
        }

        formattedIp.append(ChatColor.WHITE).append(ip.charAt(ipCharIndex));

        if (ipCharIndex + 1 < ip.length()) {
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(ipCharIndex + 1));

            if (ipCharIndex + 2 < ip.length())
                formattedIp.append(ChatColor.GOLD).append(ip.substring(ipCharIndex + 2));

            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = 50;
        }

        return ChatColor.GOLD + formattedIp.toString();
    }

}