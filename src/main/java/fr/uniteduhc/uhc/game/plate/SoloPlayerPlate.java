package fr.uniteduhc.uhc.game.plate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class SoloPlayerPlate {

    private final Player player;
    private final SquarePlate squarePlate;

    public void create() {
        squarePlate.build();
    }

}
