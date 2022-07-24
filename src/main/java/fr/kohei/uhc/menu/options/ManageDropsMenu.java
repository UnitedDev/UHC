package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.menu.options.rate.AppleRateMenu;
import fr.kohei.uhc.menu.options.rate.FlintRateMenu;
import fr.kohei.uhc.menu.options.rate.PearlRateMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManageDropsMenu extends Menu {

    public static ItemStack getAppleRateDisplay() {
        return new ItemBuilder(Material.APPLE).setName("&6&lPommes").setLore(
                "&fPermet de modifier le taux de drops de",
                "&fpommes",
                "",
                "&8┃ &7Taux: &c" + UHC.getInstance().getGameManager().getGameConfiguration().getAppleRate() + "%",
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    public static ItemStack getFlintRateDisplay() {
        return new ItemBuilder(Material.FLINT).setName("&6&lFlint").setLore(
                "&fPermet de modifier le taux de drops de",
                "&fflint",
                "",
                "&8┃ &7Taux: &c" + UHC.getInstance().getGameManager().getGameConfiguration().getFlintRate() + "%",
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }


    public static ItemStack getPearlRateDisplay() {
        return new ItemBuilder(Material.ENDER_PEARL).setName("&6&lEnder Pearl").setLore(
                "&fPermet de modifier le taux de drops d'enders",
                "&fpearls",
                "",
                "&8┃ &7Taux: &c" + UHC.getInstance().getGameManager().getGameConfiguration().getPearlRate() + "%",
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Drops";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 1, 7, 8, 9, 17, 18, 19, 25, 26}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(8).toItemStack()));
        }

        buttons.put(12, new AppleRateButton());
        buttons.put(13, new FlintRateButton());
        buttons.put(14, new PearlRateButton());

        if (oldMenu != null) {
            buttons.put(22, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class AppleRateButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getAppleRateDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new AppleRateMenu(new ManageDropsMenu(oldMenu)).openMenu(player);
        }
    }

    public class FlintRateButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getFlintRateDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new FlintRateMenu(new ManageDropsMenu(oldMenu)).openMenu(player);
        }
    }

    public class PearlRateButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getPearlRateDisplay();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PearlRateMenu(new ManageDropsMenu(oldMenu)).openMenu(player);
        }
    }
}
