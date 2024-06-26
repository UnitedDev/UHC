package fr.uniteduhc.uhc.menu;

import fr.uniteduhc.uhc.menu.options.*;
import fr.uniteduhc.uhc.module.Module;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.GlassMenu;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameState;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.menu.options.rate.ManageOresLimitMenu;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import fr.uniteduhc.uhc.menu.options.*;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ManageOptionsMenu extends GlassMenu {
    private final Menu oldMenu;

    @Override
    public String getTitle(Player player) {
        return "Options de la partie";
    }

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Module module = UHC.getInstance().getModuleManager().getModule();

        buttons.put(2, new NetherButton());
        buttons.put(10, new PotionLimitButton());
        buttons.put(18, new DropsButton());
        buttons.put(6, new StartInventoryButton());
        buttons.put(16, new DeathInventoryButton());
        buttons.put(26, new OresLimitButton());

        buttons.put(47, new PvPTimerButton());
        buttons.put(51, new BorderTimeButton());
//        buttons.put(37, new EnchantsLimitsButton());
//        buttons.put(43, new SettingsMenu.CycleButton());

        if (module.hasRoles()) {
            buttons.put(4, new HiddenCompositionButton());
            buttons.put(22, new RolesTimerButton());
            buttons.put(31, new ManageRolesButton());
        }

        if (oldMenu != null) {
            buttons.put(49, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class PvPTimerButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return getPvPItem();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PvPTimerMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public static ItemStack getBorderTimeItem() {
        GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.STAINED_GLASS).setDurability(4).setName("&6&lBordure").setLore(
                "&fPermet de changer la durée avant l'activation",
                "&fde la bordure",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getMeetupTimer() * 1000L),
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    private class BorderTimeButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return getBorderTimeItem();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new MeetupTimerMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    private static class NetherButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean nether = UHC.getInstance().getGameManager().getGameConfiguration().isNether();
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
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            gameConfiguration.setNether(!gameConfiguration.isNether());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private class OresLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.DIAMOND).setName("&6&lLimite de minerais").setLore(
                    "&fPermet de limite le nombre de minerais",
                    "&fde diamants et d'ors pendant la partie",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageOresLimitMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
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
            new StartInventoryMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    private class DeathInventoryButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ENDER_CHEST).setName("&6&lInventaire de mort").setLore(
                    "&fPermet de modifier l'inventaire qui sera",
                    "&fdrop à la mort de quelqu'un",
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if(UHC.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
                player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
                return;
            }

            new DeathInventoryMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public static ItemStack getCycleItem() {
        return new ItemBuilder(Material.COMPASS).setName("&6&lDurée du cyle jour/nuit").setLore(
                "&fPermet de modifier la durée du cycle",
                "&fjour/nuit de la partie",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(UHC.getInstance().getGameManager().getGameConfiguration().getCycle() * 1000L),
                "",
                "&f&l» &eCliquez-ici pour y accéder"
        ).toItemStack();
    }

    private static class PotionLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean potions = UHC.getInstance().getGameManager().getGameConfiguration().isPotions();
            return new ItemBuilder(Material.POTION).setName("&6&lPotions").setLore(
                    "&fVous permet de limiter la fabrication",
                    "&fde certaines potions",
                    "",
                    "&fPotions: " + (potions ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            boolean potions = UHC.getInstance().getGameManager().getGameConfiguration().isPotions();
            UHC.getInstance().getGameManager().getGameConfiguration().setPotions(!potions);
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
            new ManageDropsMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public static ItemStack getPvPItem() {
        GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.DIAMOND_SWORD).setName("&6&lActivation du PvP").setLore(
                "&fPermet de changer la durée avant l'activation",
                "&fdu pvp",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getPvpTimer() * 1000L),
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    private static class HiddenCompositionButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean compo = UHC.getInstance().getGameManager().getGameConfiguration().isHideComposition();
            return new ItemBuilder(Material.WEB).setName("&6&lComposition cachée").setLore(
                    "&fPermet de définir si la composition sera",
                    "&fcachée ou non.",
                    "",
                    "&fComposition: " + (compo ? "&cCachée" : "&aNon Cachée"),
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            gameConfiguration.setHideComposition(!gameConfiguration.isHideComposition());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    public static ItemStack getRolesTimerItem() {
        GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.WATCH).setName("&6&lAnnonce des rôles").setLore(
                "&fPermet de changer la durée avant l'annonce",
                "&fdes rôles",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getRolesTime() * 1000L),
                "",
                "&f&l» &eCliquez-ici pour modifier"
        ).toItemStack();
    }

    public class RolesTimerButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return getRolesTimerItem();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new RolesTimerMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public class ManageRolesButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.NETHER_STAR).setName("&6&lRôles").setLore(
                    "&fPermet d'activer ou de désactiver des",
                    "&frôles",
                    "",
                    "&f&l» &eCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

        }
    }


}
