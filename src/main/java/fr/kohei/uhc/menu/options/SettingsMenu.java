package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
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

import static fr.kohei.uhc.menu.ManageOptionsMenu.getCycleItem;

@RequiredArgsConstructor
public class SettingsMenu extends GlassMenu {

    private final Menu oldMenu;

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public String getTitle(Player player) {
        return "Paramètres";
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (oldMenu != null) {
            buttons.put(40, new BackButton(oldMenu));
        }

        buttons.put(12, new TeamsButton());
        buttons.put(13, new DropsButton());
        buttons.put(14, new PotionLimitButton());

        buttons.put(21, new StartInventoryButton());
        buttons.put(22, new NetherButton());
        buttons.put(23, new BorderButton());

        return buttons;
    }

    private static class HiddenCompositionButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean compo = UHC.getGameManager().getGameConfiguration().isHideComposition();
            return new ItemBuilder(Material.WEB).setName("&6&lComposition cachée").setLore(
                    "&fPermet de définir si la composition sera",
                    "&fcachée ou non.",
                    "",
                    "&8┃ &7Composition: " + (compo ? "&cCachée" : "&aNon Cachée"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            gameConfiguration.setHideComposition(!gameConfiguration.isHideComposition());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private class DropsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.APPLE).setName("&6&lTaux de drop").setLore(
                    "&fPermet de modifier les taux de drops",
                    "&fpour certains items",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageDropsMenu(new ConfigurationMenu()).openMenu(player);
        }
    }

    private class CycleButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getCycleItem();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new CycleTimerMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    private class StartInventoryButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).setName("&6&lInventaire de départ").setLore(
                    "&fPermet de modifier l'inventaire qui sera",
                    "&fdonné en début de partie",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new StartInventoryMenu(new SettingsMenu(oldMenu)).openMenu(player);
        }
    }

    private class TeamsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BANNER).setName("&6&lGestion des équipes").setLore(
                    "&fPermet de modifier la taille des teams",
                    "&fainsi que leurs paramètres",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
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
            return new ItemBuilder(Material.GOLDEN_APPLE).setName("&6&lAbsorption").setLore(
                    "&fPermet de modifier l'effet d'absorption",
                    "&fdans la partie.",
                    "",
                    "&8┃ &7Absorption: " + (gameConfiguration.isAbsorption() ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
            gameConfiguration.setAbsorption(!gameConfiguration.isAbsorption());
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
            return new ItemBuilder(Material.POTION).setName("&6&lPotions").setLore(
                    "&fVous permet de limiter la fabrication",
                    "&fde certaines potions",
                    "",
                    "&8┃ &7Potions: " + (potions ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
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
            return new ItemBuilder(Material.NETHERRACK).setName("&6&lNether").setLore(
                    "&fPermet de modifier l'accès au nether pendant",
                    "&fla partie.",
                    "",
                    "&fNether: " + (nether ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
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
            return new ItemBuilder(Material.STAINED_GLASS).setName("&6&lGestion de la bordure").setDurability(9).setLore(
                    "&fPermet de modifier la taille et la durée",
                    "&fde la bordure",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageBorderMenu(new SettingsMenu(oldMenu)).openMenu(player);
        }
    }
}
