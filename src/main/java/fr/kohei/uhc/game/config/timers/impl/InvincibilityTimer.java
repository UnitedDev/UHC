package fr.kohei.uhc.game.config.timers.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.timers.CustomTimer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;

public class InvincibilityTimer extends CustomTimer {

    public InvincibilityTimer() {
        super(UHC.getGameManager().getGameConfiguration().getDamageTimer());
    }

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "&8❘ &cInvincibilité &8» &cDésactivée"));
        Bukkit.broadcastMessage(ChatUtil.prefix("&fVous pouvez maintenant &cprendre &fdes &cdégâts&f."));
    }

    @Override
    public void onSecond() {
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "&8❘ &cInvincibilité &8» &f" + getTimer() + " secondes"));
    }
}
