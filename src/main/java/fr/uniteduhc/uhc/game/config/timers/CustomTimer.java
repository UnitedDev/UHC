package fr.uniteduhc.uhc.game.config.timers;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public abstract class CustomTimer {

    private final GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();

    private int timer;

    public CustomTimer(int timer) {
        this.timer = timer;
    }

    public void onStart() {
        new BukkitRunnable() {
            @Override
            public void run() {
                timer--;
                onSecond();
                if(timer <= 0) {
                    cancel();
                    onEnable();
                }
            }
        }.runTaskTimer(UHC.getInstance(), 0, 20);
    }


    public abstract void onEnable();
    public void onSecond() {

    }


}
