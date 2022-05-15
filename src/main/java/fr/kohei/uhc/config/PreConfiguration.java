package fr.kohei.uhc.config;

import fr.kohei.uhc.game.config.GameConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PreConfiguration {

    private GameConfiguration gameConfiguration;
    private String game;
    private boolean publicConfiguration;

}
