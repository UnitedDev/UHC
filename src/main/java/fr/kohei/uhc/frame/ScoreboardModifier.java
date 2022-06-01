package fr.kohei.uhc.frame;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.timers.Timers;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.ModuleManager;
import fr.kohei.uhc.utils.LocationUtils;
import fr.kohei.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
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
public class ScoreboardModifier {
    private final Player player;
    private final UUID uuid;
    private final ObjectiveSign toReturn;

    ScoreboardModifier(Player player) {
        this.player = player;
        uuid = player.getUniqueId();
        toReturn = new ObjectiveSign("sidebar", "§6§lKOHEI");

        reloadData();
        toReturn.addReceiver(player);
    }

    public void reloadData() {
    }

    public void setLines(String ip) {

        UPlayer uPlayer = UPlayer.get(player);
        ModuleManager moduleManager = UHC.getModuleManager();
        GameManager gameManager = UHC.getGameManager();

        toReturn.setLine(0, "&b&c&l &e");
        if (gameManager.getGameState() == GameState.LOBBY || gameManager.getGameState() == GameState.TELEPORTATION) {
            toReturn.setLine(2, " &8┃ &fHost: &c" + gameManager.getHost());
            toReturn.setLine(3, " &8┃ &fJoueurs: &c" + gameManager.getSize() + "&8/&c" + gameManager.getGameConfiguration().getSlots());
            toReturn.setLine(4, " &8┃ &fMode: &c" + (moduleManager.getModule() == null ? "&cAucun" : moduleManager.getModule().getName()));
            if (gameManager.getGameConfiguration().getTeams() > 1) {
                int teamSize = gameManager.getGameConfiguration().getTeams();
                toReturn.setLine(5, " &8┃ &fTeams: &c" + teamSize + "vs" + teamSize);
            }
        } else {
            int distance;
            String arrow;
            if (gameManager.getUhcWorld() != player.getWorld()) {
                distance = 0;
                arrow = "•";
            } else {
                Location location = gameManager.getCenter().clone();
                location.setY(player.getLocation().getY());
                distance = (int) location.distance(player.getLocation());
                arrow = LocationUtils.getArrow(player.getLocation().clone(), gameManager.getCenter());
            }

            toReturn.setLine(2, " &8┃ &fDurée: &c" + TimeUtil.niceTime(gameManager.getDuration() * 1000L));
            toReturn.setLine(3, " &8┃ &fJoueurs: &c" + gameManager.getSize());
            toReturn.setLine(4, "&1&9&l");
            toReturn.setLine(6, " &8┃ &fPvP: &c" + (Timers.PVP.isLoading() ? TimeUtil.niceTime(Timers.PVP.getCustomTimer().getTimer() * 1000L) : "&aActivé"));
            if (Timers.BORDER.isLoading()) {
                toReturn.setLine(7, " &8┃ &fBordure: &c" + TimeUtil.niceTime(Timers.BORDER.getCustomTimer().getTimer() * 1000L));
            } else {
                toReturn.setLine(7, " &8┃ &fBordure: &c± " + (int) gameManager.getUhcWorld().getWorldBorder().getSize() / 2);
            }
            toReturn.setLine(8, "&1&9&l &c");
            toReturn.setLine(10, " &8┃ &fKills: &c" + uPlayer.getKills());
            toReturn.setLine(11, " &8┃ &fCentre: &c" + distance + "m " + arrow);
        }
        toReturn.setLine(12, "&9");
        toReturn.setLine(13, ip);

        toReturn.updateLines();

    }

    public void onLogout() {
        toReturn.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}