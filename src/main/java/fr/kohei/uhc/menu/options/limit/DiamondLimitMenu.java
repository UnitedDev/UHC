package fr.kohei.uhc.menu.options.limit;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.menu.options.rate.ManageOresLimitMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DiamondLimitMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Diamond Limit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeDiamondLimit(-10, 1));
        buttons.put(1, new ChangeDiamondLimit(-5, 14));
        buttons.put(2, new ChangeDiamondLimit(-1, 11));

        buttons.put(4, new DisplayButton(ManageOresLimitMenu.getDiamondLimitDisplay()));

        buttons.put(6, new ChangeDiamondLimit(1, 12));
        buttons.put(7, new ChangeDiamondLimit(5, 10));
        buttons.put(8, new ChangeDiamondLimit(10, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeDiamondLimit extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getDiamondLimit() + add <= 0) {
                gameConfiguration.setDiamondLimit(0);
                return;
            }
            gameConfiguration.setDiamondLimit(Math.min(gameConfiguration.getDiamondLimit() + add, 50));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
