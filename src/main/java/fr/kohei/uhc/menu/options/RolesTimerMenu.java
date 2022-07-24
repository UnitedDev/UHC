package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.menu.ManageOptionsMenu;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RolesTimerMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Annonce des rôles";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeRolesTimer(-(5 * 60), 1));
        buttons.put(1, new ChangeRolesTimer(-(2 * 60), 14));
        buttons.put(2, new ChangeRolesTimer(-(30), 11));

        buttons.put(4, new DisplayButton(ManageOptionsMenu.getRolesTimerItem()));

        buttons.put(6, new ChangeRolesTimer(30, 12));
        buttons.put(7, new ChangeRolesTimer(2 * 60, 10));
        buttons.put(8, new ChangeRolesTimer(5 * 60, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeRolesTimer extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            int add = this.add;
            String s;
            if(add < 0) {
                s = "-&c";
                add = -add;
            } else {
                s = "&a+";
            }
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(s + TimeUtil.niceTime(add * 1000L)).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getRolesTime() + add <= 60) {
                gameConfiguration.setRolesTime(60);
                return;
            }
            gameConfiguration.setRolesTime(Math.min(gameConfiguration.getRolesTime() + add, 30 * 60));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
