package fr.uniteduhc.uhc.game.config.timers.impl;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.timers.CustomTimer;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.Title;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;

@Getter
@Setter
public class BorderTimer extends CustomTimer {

    public BorderTimer() {
        super(UHC.getInstance().getGameManager().getGameConfiguration().getMeetupTimer());
    }

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "&8❘ &cBordure &8» &aActivé"));
        Bukkit.broadcastMessage(ChatUtil.prefix("&fLa &cbordure &fcommence à se reduire."));
        play();
    }

    @Override
    public void onSecond() {
        if (getTimer() <= 30) {
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "&8❘ &cBordure &8» &f" + getTimer() + " secondes"));
        }
    }

    public void play() {
        WorldBorder worldBorder = UHC.getInstance().getGameManager().getUhcWorld().getWorldBorder();
        int finalSize = UHC.getInstance().getGameManager().getGameConfiguration().getBorderEndSize();
        int blocksSecond = UHC.getInstance().getGameManager().getGameConfiguration().getBorderSpeed();
        double size = worldBorder.getSize();
        double dif = Math.abs(size - finalSize);
        double time = dif / blocksSecond;
        worldBorder.setSize(finalSize, (long) time);
    }

}
