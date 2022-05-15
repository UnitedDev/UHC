package fr.kohei.uhc.game.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnchantRuleManager {

    private final GameConfiguration gameConfiguration;
    private int pieces, bowLimit;
    private boolean ironLimit, swordLimit;

    public EnchantRuleManager(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;

        this.pieces = 4;
        this.bowLimit = 3;
        this.ironLimit = false;
    }
}
