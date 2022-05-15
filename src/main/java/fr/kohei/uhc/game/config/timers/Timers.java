package fr.kohei.uhc.game.config.timers;

import fr.kohei.uhc.game.config.timers.impl.BorderTimer;
import fr.kohei.uhc.game.config.timers.impl.InvincibilityTimer;
import fr.kohei.uhc.game.config.timers.impl.PvPTimer;
import fr.kohei.uhc.game.config.timers.impl.Roles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
public enum Timers {

    INVINCIBILITY(InvincibilityTimer.class),
    PVP(PvPTimer.class),
    BORDER(BorderTimer.class),
//    FINAL_HEAL(FinalHeal.class),
    ROLES(Roles.class);

    private final Class<? extends CustomTimer> clazz;
    private CustomTimer customTimer;

    public void setCustomTimer(CustomTimer customTimer) {
        this.customTimer = customTimer;
    }

    @SneakyThrows
    public static void onStart() {
        for (Timers timers : values()) {
            timers.setCustomTimer(timers.getClazz().newInstance());
            timers.getCustomTimer().onStart();
        }
    }

    public boolean isLoading() {
        return customTimer == null || customTimer.getTimer() > 0;
    }

}
