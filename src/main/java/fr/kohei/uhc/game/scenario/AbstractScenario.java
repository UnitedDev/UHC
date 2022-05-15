package fr.kohei.uhc.game.scenario;

import fr.kohei.menu.Menu;
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
