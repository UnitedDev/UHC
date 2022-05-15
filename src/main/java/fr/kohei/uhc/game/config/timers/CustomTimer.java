package fr.kohei.uhc.game.config.timers;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public abstract class CustomTimer {

    private final GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();

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
        }.runTaskTimer(UHC.getPlugin(), 0, 20);
    }


    public abstract void onEnable();
    public void onSecond() {

    }


}
