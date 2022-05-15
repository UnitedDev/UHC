package fr.kohei.uhc.menu.options;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.EnchantRuleManager;
import fr.kohei.uhc.game.config.RulesManager;
import fr.kohei.uhc.menu.ConfigurationMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class EnchantmentManager extends Menu {

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

        buttons.put(4, new ConfigurationMenu.EnchantsLimitsButton());

        buttons.put(21, new ProjectilesButton());
        buttons.put(22, new ChangeBowEnchantButton());
        buttons.put(23, new ChangePiecesButton());

        buttons.put(29, new ChangeFlintButton());
        buttons.put(30, new ChangeSwordEnchantButton());
        buttons.put(31, new ChangeFlameButton());
        buttons.put(32, new ChangeIronEnchantButton());
        buttons.put(33, new ChangePunchItem());

        return buttons;
    }


    private static class ChangeIronEnchantButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            return new ItemBuilder(Material.IRON_CHESTPLATE).setName("&cLimite de protection sur le fer").setLore(
                    "&fPermet de modifier la limite d'enchant",
                    "&fqu'il y aura pour l'armure en fer.",
                    "",
                    "&fLimite: &c" + (enchantManager.isIronLimit() ? "&cProtection 2" : "&cProtection 3"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            enchantManager.setIronLimit(!enchantManager.isIronLimit());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangeSwordEnchantButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            return new ItemBuilder(Material.IRON_SWORD).setName("&cLimite de sharpness sur le fer").setLore(
                    "&fPermet de modifier la limite de sharpness",
                    "&fd'une épée en fer.",
                    "",
                    "&fLimite: &c" + (enchantManager.isSwordLimit() ? "&cSharpness 3" : "&cSharpness 4"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            enchantManager.setSwordLimit(!enchantManager.isSwordLimit());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangePiecesButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            return new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("&cLimite de pièces").setLore(
                    "&fPermet de modifier la limite des pièces",
                    "&fen diamant pour chaque joueur.",
                    "",
                    "&fLimite: &c" + enchantManager.getPieces() + " pièces",
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();

            if (clickType.isLeftClick()) {
                int newAmount = enchantManager.getPieces() + 1;
                if(newAmount >= 4) {
                    newAmount = 4;
                }
                enchantManager.setPieces(newAmount);
            } else {
                int newAmount = enchantManager.getPieces() - 1;
                if(newAmount <= 0) {
                    newAmount = 0;
                }
                enchantManager.setPieces(newAmount);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ProjectilesButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            return new ItemBuilder(Material.FISHING_ROD).setName("&cFishing Rod").setLore(
                    "&fPermet d'activer ou de désactiver les",
                    "&fprojectiles.",
                    "",
                    "&fRod: " + (enchantManager.getGameConfiguration().getRulesManager().isFinishRod() ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            RulesManager enchantManager = UHC.getGameManager().getGameConfiguration().getRulesManager();
            enchantManager.setFinishRod(!enchantManager.isFinishRod());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangeBowEnchantButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            return new ItemBuilder(Material.BOW).setName("&cPower").setLore(
                    "&fPermet de modifier la limite d'enchant",
                    "&fpour l'arc.",
                    "",
                    "&fLimite: &cPower " + enchantManager.getBowLimit(),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            EnchantRuleManager enchantManager = UHC.getGameManager().getGameConfiguration().getEnchantManager();
            if (clickType.isLeftClick()) {
                int newAmount = enchantManager.getBowLimit() + 1;
                if (newAmount >= 4) {
                    newAmount = 4;
                }
                enchantManager.setBowLimit(newAmount);
            } else {
                int newAmount = enchantManager.getBowLimit() - 1;
                if (newAmount <= 2) {
                    newAmount = 2;
                }
                enchantManager.setBowLimit(newAmount);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangeFlintButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.FLINT_AND_STEEL).setName("&cBriquet").setLore(
                    "&fPermet d'activer ou de désactiver l'usage",
                    "&fdu briquet pendant la partie.",
                    "",
                    "&fBriquet: " + (UHC.getGameManager().getGameConfiguration().getRulesManager().isFlintAndSteel() ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            RulesManager rulesManager = UHC.getGameManager().getGameConfiguration().getRulesManager();
            rulesManager.setFlintAndSteel(!rulesManager.isFlintAndSteel());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangeFlameButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.MAGMA_CREAM).setName("&cFlame").setLore(
                    "&fPermet d'activer ou de désactiver l'usage",
                    "&fde l'enchantement flame.",
                    "",
                    "&fFlame: " + (UHC.getGameManager().getGameConfiguration().getRulesManager().isFlame() ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            RulesManager rulesManager = UHC.getGameManager().getGameConfiguration().getRulesManager();
            rulesManager.setFlame(!rulesManager.isFlame());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private static class ChangePunchItem extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ARROW).setName("&cPunch").setLore(
                    "&fPermet d'activer ou de désactiver l'usage",
                    "&fde l'enchantement punch.",
                    "",
                    "&fPunch: " + (UHC.getGameManager().getGameConfiguration().getRulesManager().isPunch() ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            RulesManager rulesManager = UHC.getGameManager().getGameConfiguration().getRulesManager();
            rulesManager.setPunch(!rulesManager.isPunch());
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
