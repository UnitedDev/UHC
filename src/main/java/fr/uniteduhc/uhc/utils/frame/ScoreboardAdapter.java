package fr.uniteduhc.uhc.utils.frame;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameManager;
import fr.uniteduhc.uhc.game.GameState;
import fr.uniteduhc.uhc.game.config.timers.Timers;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.module.ModuleManager;
import fr.uniteduhc.uhc.utils.LocationUtils;
import fr.uniteduhc.utils.TimeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardAdapter implements FrameAdapter {

    @Override
    public String getTitle(final Player player) {
        return "&6&lUNITED";
    }

    @Override
    public List<String> getLines(final Player player) {
        final List<String> toReturn = new ArrayList<>();
        UPlayer uPlayer = UPlayer.get(player);
        ModuleManager moduleManager = UHC.getInstance().getModuleManager();
        GameManager gameManager = UHC.getInstance().getGameManager();

        toReturn.add("");
        if (gameManager.getGameState() == GameState.LOBBY || gameManager.getGameState() == GameState.TELEPORTATION) {
            toReturn.add(" &8┃ &fHost: &a" + gameManager.getHost());
            toReturn.add(" &8┃ &fJoueurs: &c" + gameManager.getSize() + "&8/&c" + gameManager.getGameConfiguration().getSlots());
            toReturn.add(" &8┃ &fMode: &e" + (moduleManager.getModule() == null ? "&cAucun" : moduleManager.getModule().getName()));
            if (gameManager.getGameConfiguration().getTeams() > 1) {
                int teamSize = gameManager.getGameConfiguration().getTeams();
                toReturn.add(" &8┃ &fTeams: &c" + teamSize + "vs" + teamSize);
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

            toReturn.add(" &8┃ &fDurée: &c" + TimeUtil.niceTime(gameManager.getDuration() * 1000L));
            toReturn.add(" &8┃ &fJoueurs: &a" + gameManager.getSize());
            toReturn.add(" &8┃ &fGroupes: &e" + gameManager.getGameConfiguration().getGroups());
            toReturn.add("&1&9&l");
            toReturn.add(" &8┃ &fPvP: &c" + (Timers.PVP.isLoading() ? TimeUtil.niceTime(Timers.PVP.getCustomTimer().getTimer() * 1000L) : "&aActivé"));
            if (Timers.BORDER.isLoading()) {
                toReturn.add(" &8┃ &fBordure: &c" + TimeUtil.niceTime(Timers.BORDER.getCustomTimer().getTimer() * 1000L));
            } else {
                DecimalFormat df = new DecimalFormat("##.#");
                toReturn.add(" &8┃ &fBordure: &c± " + df.format(gameManager.getUhcWorld().getWorldBorder().getSize() / 2));
            }
            toReturn.add("&1&9&l &c");
            toReturn.add(" &8┃ &fCentre: &a" + distance + "m " + arrow);
            toReturn.add(" &8┃ &fKills: &c" + uPlayer.getKills());
        }
        toReturn.add("&3");
        toReturn.add("&e        " + ip.get(index));

        return toReturn;
    }

    public int index = 0;
    List<String> ip = Arrays.asList(
            "§6uniteduhc.fr",
            "§6§eu§6nited.fr",
            "§6u§en§6ited.fr",
            "§6un§ei§6ted.fr",
            "§6uni§et§6ed.fr",
            "§6unit§ee§6d.fr",
            "§6unite§ed§6.fr",
            "§6united§e.§6fr",
            "§6united.§ef§6r",
            "§6united.f§er",
            "§euniteduhc.fr",
            "§euniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§euniteduhc.fr",
            "§euniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr",
            "§6uniteduhc.fr"
    );
}
