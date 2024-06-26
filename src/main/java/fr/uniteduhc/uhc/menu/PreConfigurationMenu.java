package fr.uniteduhc.uhc.menu;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.config.PreConfiguration;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.Heads;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PreConfigurationMenu extends PaginatedMenu {
    private final boolean personnal;
    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Pre-Config";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(3, new PrivateConfigButton());
        buttons.put(4, new SaveConfigButton());
        buttons.put(5, new DisplayButton(new ItemBuilder(Heads.SOON.toItemStack()).setName("&6&lSoon").toItemStack()));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

//        for (PreConfiguration preConfiguration : UHC.getInstance().get().get(player.getUniqueId()).getPreConfigurations()) {
//            buttons.put(buttons.size(), new PreConfigurationButton(preConfiguration));
//        }

        return buttons;
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @RequiredArgsConstructor
    private static class PreConfigurationButton extends Button {

        private final PreConfiguration preConfiguration;

        @Override
        public ItemStack getButtonItem(Player player) {
            GameConfiguration gameConfiguration = preConfiguration.getGameConfiguration();
            return new ItemBuilder(Material.GOLDEN_APPLE).setName("&6&l" + gameConfiguration.getCustomName()).setLore(
                    "",
                    "&f➥ &c&lConfiguration",
                    " &fSlots: &e" + gameConfiguration.getSlots(),
                    " &fTeams: &a" + gameConfiguration.getTeams(),
                    " &fBordure:",
                    " &7▪ &fStart: &c± " + gameConfiguration.getBorderStartSize(),
                    " &7▪ &fFin: &c± " + gameConfiguration.getBorderEndSize(),
                    "",
                    "&f➥ &c&lTimers",
                    " &fPvP: &a" + TimeUtil.niceTime(gameConfiguration.getPvpTimer()),
                    " &fBordure: &a" + TimeUtil.niceTime(gameConfiguration.getMeetupTimer()),
                    " &fCycle: &a" + TimeUtil.niceTime(gameConfiguration.getCycle()),
                    "",
                    "&f&l» &eClic-droit pour charger la config",
                    "&f&l» &eDrop pour supprimer la config"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType == ClickType.DROP) {
//                UHCPlayerCache cache = UHC.getInstance().getPlayerCache().get(player.getUniqueId());
//                cache.getPreConfigurations().remove(preConfiguration);
//                UHC.getInstance().getPlayerCache().save(player.getUniqueId(), cache);
                return;
            }

            UHC.getInstance().getGameManager().applyConfiguration(preConfiguration);
            player.sendMessage(ChatUtil.prefix("&fVous avez &achargé &fla pre-config avec &asuccès"));
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class SaveConfigButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Heads.BALLOON_GREEN.toItemStack()).setName("&aSauvegarder").setLore(
                    "&fPermet de sauvegarder la pré-configuration",
                    "&fafin qu'elle soit réutilisable",
                    "",
                    "&f&l» &eCliquez-ici pour sauvegarder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
//            UHCPlayerCache cache = UHC.getInstance().getPlayerCache().get(player.getUniqueId());
            PreConfiguration config = new PreConfiguration(
                    UHC.getInstance().getGameManager().getGameConfiguration(),
                    UHC.getInstance().getModuleManager().getModule().getName(),
                    false
            );

//            cache.getPreConfigurations().removeIf(preConfiguration ->
//                    preConfiguration.getGameConfiguration().getCustomName().equals(config.getGameConfiguration().getCustomName())
//            );
//
//            cache.getPreConfigurations().add(config);
//            UHC.getInstance().getPlayerCache().save(player.getUniqueId(), cache);
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    private class PrivateConfigButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Heads.BALLOON_ORANGE.toItemStack()).setName("&6&lMes Pre-Config" + (personnal ? " &8(&aSéléctionné&8)" : ""))  .setLore(
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PreConfigurationMenu(true, oldMenu).openMenu(player);
        }
    }

}
