package fr.uniteduhc.uhc.menu.border;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.menu.ManageBorderMenu;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InitialBorderMenu extends Menu {

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Bordure initiale";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ChangeBorderStartSize(-250, 1));
        buttons.put(1, new ChangeBorderStartSize(-100, 14));
        buttons.put(2, new ChangeBorderStartSize(-50, 11));

        buttons.put(4, new DisplayButton(ManageBorderMenu.getInitialBorderDisplay()));

        buttons.put(6, new ChangeBorderStartSize(50, 12));
        buttons.put(7, new ChangeBorderStartSize(100, 10));
        buttons.put(8, new ChangeBorderStartSize(250, 2));

        if (oldMenu != null) {
            buttons.put(13, new BackButton(oldMenu));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class ChangeBorderStartSize extends Button {

        private final int add;
        private final int durability;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setDurability(durability).setName(add > 0 ? "&a+" + add : "&c" + add).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            if (gameConfiguration.getBorderStartSize() + add <= 100) {
                gameConfiguration.setBorderStartSize(100);
                return;
            }

            int size = gameConfiguration.getBorderStartSize() + add;

            gameConfiguration.setBorderStartSize(Math.min(size, 1500));

            if(gameConfiguration.getGameManager().getUhcWorld() != null) {
                gameConfiguration.getGameManager().getUhcWorld().getWorldBorder().setSize(2 * gameConfiguration.getBorderStartSize());
            }

            new InitialBorderMenu(oldMenu).openMenu(player);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
