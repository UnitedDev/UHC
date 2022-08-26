package fr.uniteduhc.uhc.menu;


import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManagerSlotsMenu extends Menu {

    private final Menu oldMenu;


    @Override
    public String getTitle(Player player) {
        return "Slots";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeSlotsButton(-10, 1));
        buttons.put(1, new ChangeSlotsButton(-5, 14));
        buttons.put(2, new ChangeSlotsButton(-1, 11));

        buttons.put(4, new ConfigurationMenu.SlotsButton());

        buttons.put(6, new ChangeSlotsButton(1, 12));
        buttons.put(7, new ChangeSlotsButton(5, 10));
        buttons.put(8, new ChangeSlotsButton(10, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeSlotsButton extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getSlots() + add <= 2) {
                gameConfiguration.setSlots(2);
                return;
            }
            gameConfiguration.setSlots(Math.min(gameConfiguration.getSlots() + add, 60));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

}
