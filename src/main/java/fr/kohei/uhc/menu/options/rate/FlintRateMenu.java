package fr.kohei.uhc.menu.options.rate;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.menu.options.ManageDropsMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FlintRateMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Flint Rate";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeFlintRate(-10, 1));
        buttons.put(1, new ChangeFlintRate(-5, 14));
        buttons.put(2, new ChangeFlintRate(-1, 11));

        buttons.put(4, new DisplayButton(ManageDropsMenu.getFlintRateDisplay()));

        buttons.put(6, new ChangeFlintRate(1, 12));
        buttons.put(7, new ChangeFlintRate(5, 10));
        buttons.put(8, new ChangeFlintRate(10, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeFlintRate extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            if (gameConfiguration.getFlintRate() + add <= 0) {
                gameConfiguration.setFlintRate(0);
                return;
            }
            gameConfiguration.setFlintRate(Math.min(gameConfiguration.getFlintRate() + add, 100));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
