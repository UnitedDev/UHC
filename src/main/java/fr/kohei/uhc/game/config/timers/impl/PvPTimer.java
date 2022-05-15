package fr.kohei.uhc.game.config.timers.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.timers.CustomTimer;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;

public class PvPTimer extends CustomTimer {
    public PvPTimer() {
        super(UHC.getGameManager().getGameConfiguration().getPvpTimer());
    }

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage(ChatUtil.prefix("&fLe &cPvP &fest désormais &aactivé&f."));
        UHC.getGameManager().getUhcWorld().setPVP(true);
    }

}
