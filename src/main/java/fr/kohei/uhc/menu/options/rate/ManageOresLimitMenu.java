package fr.kohei.uhc.menu.options.rate;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.options.limit.DiamondLimitMenu;
import fr.kohei.uhc.menu.options.limit.GoldLimitMenu;
import fr.kohei.uhc.menu.options.limit.IronLimitMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManageOresLimitMenu extends Menu {

    public static ItemStack getDiamondLimitDisplay() {
        return new ItemBuilder(Material.DIAMOND_ORE).setName("&cDiamant").setLore(
                "&fPermet de modifier la limite de diamants",
                "&fqui peuvent être minés par un joueur",
                "",
                "&8┃ &7Limite: &c" + UHC.getGameManager().getGameConfiguration().getDiamondLimit(),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).setAmount(UHC.getGameManager().getGameConfiguration().getDiamondLimit()).toItemStack();
    }

    public static ItemStack getGoldLimitDisplay() {
        return new ItemBuilder(Material.GOLD_ORE).setName("&cOr").setLore(
                "&fPermet de modifier la limite d'or",
                "&fqui peuvent être minés par un joueur",
                "",
                "&8┃ &7Limite: &c" + UHC.getGameManager().getGameConfiguration().getGoldLimit(),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).setAmount(UHC.getGameManager().getGameConfiguration().getGoldLimit()).toItemStack();
    }

    public static ItemStack getIronLimitDisplay() {
        return new ItemBuilder(Material.IRON_ORE).setName("&cFer").setLore(
                "&fPermet de modifier la limite de fer",
                "&fqui peuvent être minés par un joueur",
                "",
                "&8┃ &7Limite: &c" + UHC.getGameManager().getGameConfiguration().getIronLimit(),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).setAmount(UHC.getGameManager().getGameConfiguration().getIronLimit()).toItemStack();
    }

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Minerais";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 18, 19, 25, 26}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(8).toItemStack()));
        }

        buttons.put(12, new DiamondLimitButton());
        buttons.put(13, new GoldLimitButton());
        buttons.put(14, new IronLimitDisplay());

        if (oldMenu != null) {
            buttons.put(22, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class DiamondLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getDiamondLimitDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new DiamondLimitMenu(new ManageOresLimitMenu(oldMenu)).openMenu(player);
        }
    }

    public class GoldLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getGoldLimitDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new GoldLimitMenu(new ManageOresLimitMenu(oldMenu)).openMenu(player);
        }
    }

    public class IronLimitDisplay extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getIronLimitDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new IronLimitMenu(new ManageOresLimitMenu(oldMenu)).openMenu(player);
        }
    }
}
