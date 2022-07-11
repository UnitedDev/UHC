package fr.kohei.uhc.game.scenario;

import fr.kohei.uhc.game.scenario.impl.*;
import fr.kohei.uhc.game.scenario.impl.*;
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
