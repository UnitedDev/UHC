package fr.uniteduhc.uhc.menu.options.limit;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.menu.options.rate.ManageOresLimitMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class IronLimitMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Iron Limit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeIronLimitButton(-10, 1));
        buttons.put(1, new ChangeIronLimitButton(-5, 14));
        buttons.put(2, new ChangeIronLimitButton(-1, 11));

        buttons.put(4, new DisplayButton(ManageOresLimitMenu.getIronLimitDisplay()));

        buttons.put(6, new ChangeIronLimitButton(1, 12));
        buttons.put(7, new ChangeIronLimitButton(5, 10));
        buttons.put(8, new ChangeIronLimitButton(10, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class ChangeIronLimitButton extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getIronLimit() + add <= 0) {
                gameConfiguration.setIronLimit(0);
                return;
            }
            gameConfiguration.setIronLimit(Math.min(gameConfiguration.getIronLimit() + add, 50));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
