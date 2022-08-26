package fr.uniteduhc.uhc.game.scenario.impl;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.scenario.AbstractScenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class HasteyBoys extends AbstractScenario implements Listener {
    @Override
    public String getName() {
        return "HasteyBoys";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fTous les outils que vous craftez seront",
                "&fEfficiency 3 et Unbreaking 3."
        );
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_HOE;
    }

    @Override
    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UHC.getInstance());
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Material itemType = event.getInventory().getResult().getType();
        switch (itemType) {
            case WOOD_PICKAXE:
            case WOOD_AXE:
            case WOOD_SPADE:
            case WOOD_HOE:
            case STONE_PICKAXE:
            case STONE_AXE:
            case STONE_SPADE:
            case STONE_HOE:
            case IRON_PICKAXE:
            case IRON_AXE:
            case IRON_SPADE:
            case IRON_HOE:
            case GOLD_PICKAXE:
            case GOLD_AXE:
            case GOLD_SPADE:
            case GOLD_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_AXE:
            case DIAMOND_SPADE:
            case DIAMOND_HOE:
                ItemStack item = event.getRecipe().getResult();
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
                item.setItemMeta(itemMeta);
                event.getInventory().setResult(item);
        }
    }


}
