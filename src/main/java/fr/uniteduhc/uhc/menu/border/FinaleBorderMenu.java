package fr.uniteduhc.uhc.menu.border;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.menu.ManageBorderMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FinaleBorderMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Bordure finale";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeBorderEndSize(-250, 1));
        buttons.put(1, new ChangeBorderEndSize(-100, 14));
        buttons.put(2, new ChangeBorderEndSize(-50, 11));

        buttons.put(4, new DisplayButton(ManageBorderMenu.getFinaleBorderDisplay()));

        buttons.put(6, new ChangeBorderEndSize(50, 12));
        buttons.put(7, new ChangeBorderEndSize(100, 10));
        buttons.put(8, new ChangeBorderEndSize(250, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeBorderEndSize extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getBorderEndSize() + add <= 50) {
                gameConfiguration.setBorderEndSize(50);
                return;
            }
            gameConfiguration.setBorderEndSize(Math.min(gameConfiguration.getBorderEndSize() + add, 500));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
