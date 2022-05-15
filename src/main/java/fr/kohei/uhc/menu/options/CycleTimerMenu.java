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
public class CycleTimerMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Cycle jour/nuit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeCycleTimer(-(5 * 60), 1));
        buttons.put(1, new ChangeCycleTimer(-(2 * 60), 14));
        buttons.put(2, new ChangeCycleTimer(-(30), 11));

        buttons.put(4, new DisplayButton(ManageOptionsMenu.getCycleItem()));

        buttons.put(6, new ChangeCycleTimer(30, 12));
        buttons.put(7, new ChangeCycleTimer(2 * 60, 10));
        buttons.put(8, new ChangeCycleTimer(5 * 60, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeCycleTimer extends Button {

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
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            if (gameConfiguration.getCycle() + add <= 120) {
                gameConfiguration.setCycle(120);
                return;
            }
            gameConfiguration.setCycle(Math.min(gameConfiguration.getCycle() + add, 10 * 60));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
