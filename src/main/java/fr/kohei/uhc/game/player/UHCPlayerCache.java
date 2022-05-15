package fr.kohei.uhc.game.player;

import fr.kohei.uhc.config.PreConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class UHCPlayerCache {

    private int kills;
    private int deaths;
    private int wins;
    private int games;

    private int diamonds;
    private int gold;

    private List<PreConfiguration> preConfigurations;

}
