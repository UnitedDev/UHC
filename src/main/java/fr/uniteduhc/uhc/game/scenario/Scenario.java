package fr.uniteduhc.uhc.game.scenario;

import fr.uniteduhc.uhc.game.scenario.impl.*;
import fr.uniteduhc.uhc.game.scenario.impl.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public enum Scenario {

    HASTEY_BOYS(new HasteyBoys()),
    CUTCLEAN(new CutClean()),
    TIMBER(new Timber()),
    CATEYES(new CatEyes()),
    BETAZOMBIE(new BetaZombie()),
    SAFEMINERS(new SafeMiners())

    ;

    private final AbstractScenario scenario;
    @Setter
    private boolean enabled;

}
