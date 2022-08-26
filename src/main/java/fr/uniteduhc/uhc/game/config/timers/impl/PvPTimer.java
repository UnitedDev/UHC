package fr.uniteduhc.uhc.game.config.timers.impl;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.timers.CustomTimer;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.Bukkit;

public class PvPTimer extends CustomTimer {
    public PvPTimer() {
        super(UHC.getInstance().getGameManager().getGameConfiguration().getPvpTimer());
    }

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage(ChatUtil.prefix("&fLe &cPvP &fest désormais &aactivé&f."));
        UHC.getInstance().getGameManager().getUhcWorld().setPVP(true);
    }

}
