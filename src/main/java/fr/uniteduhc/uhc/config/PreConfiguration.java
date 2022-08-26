package fr.uniteduhc.uhc.config;

import fr.uniteduhc.uhc.game.config.GameConfiguration;
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
