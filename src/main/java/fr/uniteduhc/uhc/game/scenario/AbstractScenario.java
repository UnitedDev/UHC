package fr.uniteduhc.uhc.game.scenario;

import fr.uniteduhc.menu.Menu;
import org.bukkit.Material;

import java.util.List;

public abstract class AbstractScenario {

    public abstract String getName();

    public abstract List<String> getLore();

    public abstract Material getIcon();

    public abstract void onStart();

    public Menu getMenu() {
        return null;
    }

}
