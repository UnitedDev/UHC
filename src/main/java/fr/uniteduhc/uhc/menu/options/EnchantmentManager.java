package fr.uniteduhc.uhc.menu.options;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.config.GameConfiguration;
import fr.uniteduhc.uhc.menu.EnchantmentMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class EnchantmentManager extends PaginatedMenu {

    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Limite d'enchantements";
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();


        for(Enchantment enchantment : Enchantment.values()) {
            buttons.put(buttons.size(), new EnchantmentButton(enchantment));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public static class EnchantmentButton extends Button {
        private final Enchantment enchantment;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            int level = gameConfiguration.getEnchantmentsLimit().getOrDefault(enchantment, 0);
            lore.add(" ");
            lore.add("&7Configuration: &c" + (level == 0 ? "&aIllimité" : level));
            lore.add(" ");
            lore.add("&8» &7Clic-gauche pour augmenter");
            lore.add("&8» &7Clic-droit pour diminuer");
            lore.add("&8» &7Drop pour reset");
            return new ItemBuilder(Material.ENCHANTED_BOOK).setName("&6&l" + EnchantmentMenu.fromEnchant(enchantment)).setLore(lore).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
            int level = gameConfiguration.getEnchantmentsLimit().getOrDefault(enchantment, 0);
            if(clickType == ClickType.LEFT) {
                gameConfiguration.getEnchantmentsLimit().put(enchantment, level + 1);
            } else if(clickType == ClickType.RIGHT) {
                if (level == 0) return;
                gameConfiguration.getEnchantmentsLimit().put(enchantment, level - 1);
            } else if(clickType == ClickType.DROP) {
                gameConfiguration.getEnchantmentsLimit().put(enchantment, 0);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}