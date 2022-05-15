package fr.kohei.uhc.game.config.timers.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.timers.CustomTimer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;

public class FinalHeal extends CustomTimer {
    public FinalHeal() {
        super(UHC.getGameManager().getGameConfiguration().getFinalHealTimer());
    }

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Title.sendActionBar(player, "&8❘ &cFinalHeal &8» &aActivé");
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(20F);
        });
        Bukkit.broadcastMessage(ChatUtil.prefix("&fVous avez été &dsoigné&f."));
    }

    @Override
    public void onSecond() {
        if (getTimer() <= 30) {
            Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "&8❘ &cFinalHeal &8» &f" + getTimer()));
        }
    }
}
