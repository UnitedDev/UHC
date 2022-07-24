package fr.kohei.uhc.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.menu.buttons.Glass;
import fr.kohei.uhc.UHC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GameInventoryMenu extends Menu{
    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Inventaire";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i : new int[]{0, 1, 4, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53}) {
            buttons.put(i, new Glass(4));
        }

        ItemStack[] armor = UHC.getInstance().getGameManager().getGameConfiguration().getStartArmor();
        if (armor.length > 3) {
            buttons.put(2, new DisplayButton(armor[3]));
        }
        if (armor.length > 2) {
            buttons.put(3, new DisplayButton(armor[2]));
        }
        if (armor.length > 1) {
            buttons.put(5, new DisplayButton(armor[1]));
        }
        if (armor.length > 0) {
            buttons.put(6, new DisplayButton(armor[0]));
        }

        int j = 9;
        for (ItemStack is : UHC.getInstance().getGameManager().getGameConfiguration().getStartInventory()) {
            if (is == null) buttons.put(j++, new DisplayButton(new ItemStack(Material.AIR)));
            else buttons.put(j++, new DisplayButton(is));
        }


        if (oldMenu != null) {
            buttons.put(4, new BackButton(oldMenu));
        }

        return buttons;
    }
}
