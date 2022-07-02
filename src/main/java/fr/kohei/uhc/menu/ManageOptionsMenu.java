package fr.kohei.uhc.menu;

import fr.kohei.uhc.module.Module;
import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameState;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.menu.options.rate.ManageOresLimitMenu;
import fr.kohei.uhc.menu.options.*;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import fr.kohei.uhc.menu.options.*;
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

        Module module = UHC.getModuleManager().getModule();

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
        GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.STAINED_GLASS).setDurability(4).setName("&cBordure").setLore(
                "&fPermet de changer la durée avant l'activation",
                "&fde la bordure",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getMeetupTimer() * 1000L),
                "",
                "&f&l» &cCliquez-ici pour modifier"
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

    private class OresLimitButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.DIAMOND).setName("&cLimite de minerais").setLore(
                    "&fPermet de limite le nombre de minerais",
                    "&fde diamants et d'ors pendant la partie",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
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

    private class DeathInventoryButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ENDER_CHEST).setName("&cInventaire de mort").setLore(
                    "&fPermet de modifier l'inventaire qui sera",
                    "&fdrop à la mort de quelqu'un",
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if(UHC.getGameManager().getGameState() != GameState.LOBBY) {
                player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cet item pendant la partie"));
                return;
            }

            new DeathInventoryMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public static ItemStack getCycleItem() {
        return new ItemBuilder(Material.COMPASS).setName("&cDurée du cyle jour/nuit").setLore(
                "&fPermet de modifier la durée du cycle",
                "&fjour/nuit de la partie",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(UHC.getGameManager().getGameConfiguration().getCycle() * 1000L),
                "",
                "&f&l» &cCliquez-ici pour y accéder"
        ).toItemStack();
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

    private class DropsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.APPLE).setName("&cTaux de drop").setLore(
                    "&fPermet de modifier les taux de drops",
                    "&fpour certains items",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ManageDropsMenu(new ManageOptionsMenu(oldMenu)).openMenu(player);
        }
    }

    public static ItemStack getPvPItem() {
        GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.DIAMOND_SWORD).setName("&cActivation du PvP").setLore(
                "&fPermet de changer la durée avant l'activation",
                "&fdu pvp",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getPvpTimer() * 1000L),
                "",
                "&f&l» &cCliquez-ici pour modifier"
        ).toItemStack();
    }

    private static class HiddenCompositionButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean compo = UHC.getGameManager().getGameConfiguration().isHideComposition();
            return new ItemBuilder(Material.WEB).setName("&cComposition cachée").setLore(
                    "&fPermet de définir si la composition sera",
                    "&fcachée ou non.",
                    "",
                    "&fComposition: " + (compo ? "&cCachée" : "&aNon Cachée"),
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
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

    public static ItemStack getRolesTimerItem() {
        GameConfiguration gameConfiguration = UHC.getGameManager().getGameConfiguration();
        return new ItemBuilder(Material.WATCH).setName("&cAnnonce des rôles").setLore(
                "&fPermet de changer la durée avant l'annonce",
                "&fdes rôles",
                "",
                "&8┃ &7Configuration: &c" + TimeUtil.niceTime(gameConfiguration.getRolesTime() * 1000L),
                "",
                "&f&l» &cCliquez-ici pour modifier"
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
            return new ItemBuilder(Material.NETHER_STAR).setName("&cRôles").setLore(
                    "&fPermet d'activer ou de désactiver des",
                    "&frôles",
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

        }
    }


}
