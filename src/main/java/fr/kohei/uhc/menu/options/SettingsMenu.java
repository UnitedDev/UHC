package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.menu.ConfigurationMenu;
import fr.kohei.uhc.menu.ManageBorderMenu;
import fr.kohei.uhc.menu.ManageOptionsMenu;
import fr.kohei.uhc.menu.ManageTeamsMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SettingsMenu extends Menu {

    private final Menu oldMenu;


    @Override
    public String getTitle(Player player) {
        return "Enchantements";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();


        for (int i : new int[]{1, 2, 9, 10, 18, 35, 43, 44, 51, 52}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).toItemStack()));
        }

        for (int i : new int[]{6, 7, 16, 17, 26, 27, 36, 37, 46, 47}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(1).toItemStack()));
        }

        if (oldMenu != null) {
            buttons.put(49, new BackButton(oldMenu));
        }

        buttons.put(4, new ConfigurationMenu.SettingsButton());

        buttons.put(21, new TeamsButton());
        buttons.put(22, new GoldenHeadButton());
        buttons.put(23, new PotionLimitButton());

        buttons.put(30, new StartInventoryButton());
        buttons.put(31, new NetherButton());
        buttons.put(32, new BorderButton());

        return buttons;
    }

    private class StartInventoryButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).setName("&cInventaire de départ").setLore(
                    "&fPermet de modifier l'inventaire qui sera",
                    "&fdonné en début de partie",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new StartInventoryMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    private class TeamsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setName("&cGestion des équipes").setLore(
                    "&fPermet de modifier la taille des teams",
                    "&fainsi que leurs paramètres",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).setDurability(15).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageTeamsMenu(new SettingsMenu(oldMenu)).openMenu(player);
        }
    }

    private static class GoldenHeadButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            return new ItemBuilder(Material.GOLDEN_APPLE).setName("&cGolden Head").setLore(
                    "&fPermet de modifier l'utilisation des golden",
                    "&fheads.",
                    "",
                    "&fHeads: " + (gameConfiguration.isHeads() ? "&aActivés" : "&cDésactivés"),
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            gameConfiguration.setHeads(!gameConfiguration.isHeads());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class PotionLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean potions = UHC.getGameManager().getGameConfiguration().isPotions();
            return new ItemBuilder(Material.POTION).setName("&cPotions").setLore(
                    "&fVous permet de limiter la fabrication",
                    "&fde certaines potions",
                    "",
                    "&fPotions: " + (potions ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            boolean potions = UHC.getGameManager().getGameConfiguration().isPotions();
            UHC.getGameManager().getGameConfiguration().setPotions(!potions);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class NetherButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean nether = UHC.getGameManager().getGameConfiguration().isNether();
            return new ItemBuilder(Material.NETHERRACK).setName("&cNether").setLore(
                    "&fPermet de modifier l'accès au nether pendant",
                    "&fla partie.",
                    "",
                    "&fNether: " + (nether ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            gameConfiguration.setNether(!gameConfiguration.isNether());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private class BorderButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_GLASS).setName("&cGestion de la bordure").setDurability(9).setLore(
                    "&fPermet de modifier la taille et la durée",
                    "&fde la bordure",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageBorderMenu(new SettingsMenu(oldMenu)).openMenu(player);
        }
    }
}
