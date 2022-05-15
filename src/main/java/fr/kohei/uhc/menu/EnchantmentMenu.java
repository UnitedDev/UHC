package fr.kohei.uhc.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Enchant";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new DisplayButton(player.getItemInHand()));
        buttons.put(18, new UnbreakableButton());

        return buttons;
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
            if (player.getItemInHand().containsEnchantment(enchantment)) {
                lore.add(" ");
                lore.add("&8┃ &7Level: &c" + player.getItemInHand().getEnchantmentLevel(enchantment));
            }
            lore.add(" ");
            lore.add("&f&l» &cClic-gauche pour augmenter");
            lore.add("&f&l» &cClic-droit pour diminuer");
            lore.add("&f&l» &cDrop pour reset");
            return new ItemBuilder(Material.ENCHANTED_BOOK).setName("&c" + fromEnchant(enchantment)).setLore(lore).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if(clickType == ClickType.DROP) {
                player.getItemInHand().removeEnchantment(enchantment);
                return;
            }
            if(clickType == ClickType.LEFT) {
                if(!player.getItemInHand().containsEnchantment(enchantment)) {
                    player.getItemInHand().addUnsafeEnchantment(enchantment, 1);
                    return;
                }

                int old = player.getItemInHand().getEnchantmentLevel(enchantment);
                player.getItemInHand().removeEnchantment(enchantment);
                player.getItemInHand().addUnsafeEnchantment(enchantment, old + 1);
            } else {
                if(!player.getItemInHand().containsEnchantment(enchantment)) return;

                if(player.getItemInHand().getEnchantmentLevel(enchantment) == 1) {
                    player.getItemInHand().removeEnchantment(enchantment);
                    return;
                }

                int old = player.getItemInHand().getEnchantmentLevel(enchantment);
                player.getItemInHand().removeEnchantment(enchantment);
                player.getItemInHand().addUnsafeEnchantment(enchantment, old - 1);
            }
        }



        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    public static String fromEnchant(Enchantment enchantment) {
        if (enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            return "Protection";
        } else if (enchantment.equals(Enchantment.PROTECTION_FIRE)) {
            return "Fire Protection";
        } else if (enchantment.equals(Enchantment.PROTECTION_FALL)) {
            return "Feather Falling";
        } else if (enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS)) {
            return "Blast Protection";
        } else if (enchantment.equals(Enchantment.PROTECTION_PROJECTILE)) {
            return "Projectile Protection";
        } else if (enchantment.equals(Enchantment.OXYGEN)) {
            return "Respiration";
        } else if (enchantment.equals(Enchantment.WATER_WORKER)) {
            return "Aqua Infinity";
        } else if (enchantment.equals(Enchantment.THORNS)) {
            return "Thorns";
        } else if (enchantment.equals(Enchantment.DEPTH_STRIDER)) {
            return "Depth Strider";
        } else if (enchantment.equals(Enchantment.DAMAGE_ALL)) {
            return "Sharpness";
        } else if (enchantment.equals(Enchantment.DAMAGE_UNDEAD)) {
            return "Smite";
        } else if (enchantment.equals(Enchantment.DAMAGE_ARTHROPODS)) {
            return "Bane of Arthropods";
        } else if (enchantment.equals(Enchantment.KNOCKBACK)) {
            return "Knockback";
        } else if (enchantment.equals(Enchantment.FIRE_ASPECT)) {
            return "Fire Aspect";
        } else if (enchantment.equals(Enchantment.LOOT_BONUS_MOBS)) {
            return "Looting";
        } else if (enchantment.equals(Enchantment.DIG_SPEED)) {
            return "Efficiency";
        } else if (enchantment.equals(Enchantment.SILK_TOUCH)) {
            return "Silk Touch";
        } else if (enchantment.equals(Enchantment.DURABILITY)) {
            return "Unbreaking";
        } else if (enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)) {
            return "Fortune";
        } else if (enchantment.equals(Enchantment.ARROW_DAMAGE)) {
            return "Power";
        } else if (enchantment.equals(Enchantment.ARROW_KNOCKBACK)) {
            return "Punch";
        } else if (enchantment.equals(Enchantment.ARROW_FIRE)) {
            return "Flame";
        } else if (enchantment.equals(Enchantment.ARROW_INFINITE)) {
            return "Infinity";
        } else if (enchantment.equals(Enchantment.LUCK)) {
            return "Luck of the Sea";
        } else if (enchantment.equals(Enchantment.LURE)) {
            return "Lure";
        } else {
            return "Unknown";
        }
    }

    private static class UnbreakableButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            boolean unbreakable;
            if (player.getItemInHand().hasItemMeta()) {
                unbreakable = player.getItemInHand().getItemMeta().spigot().isUnbreakable();
            } else {
                unbreakable = false;
            }
            return new ItemBuilder(Material.DIAMOND_AXE).setName("&cUnbreakable").setLore(
                    "&fPermet de mettre l'item unbreakable",
                    "&f(incassable)",
                    "",
                    "&fUnbreakable: " + (unbreakable ? "&aActivé" : "&cDésactivé"),
                    "",
                    "&f&l» &cCliquez-ici pour modifier"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (player.getItemInHand().hasItemMeta()) {
                ItemStack itemStack = new ItemBuilder(player.getItemInHand()).toItemStack();
                ItemMeta meta = itemStack.getItemMeta();
                meta.spigot().setUnbreakable(!player.getItemInHand().getItemMeta().spigot().isUnbreakable());
                itemStack.setItemMeta(meta);
                player.setItemInHand(itemStack);
            } else {
                ItemStack is = new ItemBuilder(player.getItemInHand()).toItemStack();
                ItemMeta meta = is.getItemMeta();
                meta.spigot().setUnbreakable(true);
                is.setItemMeta(meta);
                player.setItemInHand(is);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

}
