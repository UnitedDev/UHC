package fr.kohei.uhc.menu;


import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.border.BorderSpeedMenu;
import fr.kohei.uhc.menu.border.FinaleBorderMenu;
import fr.kohei.uhc.menu.border.InitialBorderMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManageBorderMenu extends Menu {

    public static ItemStack getBorderSpeedDisplay() {
        return new ItemBuilder(Material.WATCH).setName("&cVitesse de la bordure").setLore(
                "&fPermet de modifier la vitesse de la bordure",
                "&fen bloc par seconde",
                "",
                "&8┃ &7Vitesse: &c" + UHC.getGameManager().getGameConfiguration().getBorderSpeed() + " bloc(s)/s",
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).toItemStack();
    }

    public static ItemStack getFinaleBorderDisplay() {
        return new ItemBuilder(Material.STAINED_GLASS).setDurability(14).setName("&cBordure finale").setLore(
                "&fPermet de modifier la taille de la bordure",
                "&ffinale",
                "",
                "&8┃ &7Bordure finale: &c± " + UHC.getGameManager().getGameConfiguration().getBorderEndSize(),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).toItemStack();
    }

    public static ItemStack getInitialBorderDisplay() {
        return new ItemBuilder(Material.STAINED_GLASS).setDurability(3).setName("&cBordure initiale").setLore(
                "&fPermet de modifier la taille de la bordure",
                "&fpour le début de la partie",
                "",
                "&8┃ &7Bordure initiale: &c± " + UHC.getGameManager().getGameConfiguration().getBorderStartSize(),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).toItemStack();
    }

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Bordure";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 18, 19, 25, 26}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(8).toItemStack()));
        }

        buttons.put(12, new InitialBorderButton());
        buttons.put(13, new BorderSpeedButton());
        buttons.put(14, new FinaleBorderButton());

        if (oldMenu != null) {
            buttons.put(22, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class InitialBorderButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getInitialBorderDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new InitialBorderMenu(new ManageBorderMenu(oldMenu)).openMenu(player);
        }
    }

    public class BorderSpeedButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getBorderSpeedDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new BorderSpeedMenu(new ManageBorderMenu(oldMenu)).openMenu(player);
        }
    }

    public class FinaleBorderButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getFinaleBorderDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new FinaleBorderMenu(new ManageBorderMenu(oldMenu)).openMenu(player);
        }
    }
}
