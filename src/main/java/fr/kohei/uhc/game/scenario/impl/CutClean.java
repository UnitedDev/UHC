package fr.kohei.uhc.game.scenario.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.scenario.AbstractScenario;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CutClean extends AbstractScenario implements Listener {
    @Override
    public String getName() {
        return "CutClean";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fTous les minerais ainsi que la nourriture",
                "&fseront directement cuits."
        );
    }

    @Override
    public Material getIcon() {
        return Material.IRON_INGOT;
    }

    @Override
    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UHC.getInstance());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e) {

        Block block = e.getBlock();

        Location loc = new Location(block.getWorld(), block.getLocation().getBlockX() + 0.5D, block.getLocation().getBlockY() + 0.5D, block.getLocation().getBlockZ() + 0.5D);
        if (block.getType() == Material.IRON_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
            e.getPlayer().giveExp(2 * 2);
        }
        if (block.getType() == Material.COAL_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.COAL, 1));
            e.getPlayer().giveExp(2);
        }
        if (block.getType() == Material.GOLD_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
            e.getPlayer().giveExp(2 * 3);
        }
        if (block.getType() == Material.DIAMOND_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
            e.getPlayer().giveExp(2 * 4);
        }


        if (block.getType() == Material.QUARTZ_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.QUARTZ, 3));
            e.getPlayer().giveExp(2 * 6);
        }
        if (block.getType() == Material.EMERALD_ORE) {
            block.setType(Material.AIR);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.EMERALD, 3));
            e.getPlayer().giveExp(2 * 10);
        }
    }


    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.PIG) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.GRILLED_PORK, 2));
        }

        if (e.getEntityType() == EntityType.COW) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_BEEF, 2));
            e.getDrops().add(new ItemStack(Material.LEATHER));
        }

        if (e.getEntityType() == EntityType.CHICKEN) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 2));
            e.getDrops().add(new ItemStack(Material.FEATHER, 2));
        }

        if (e.getEntityType() == EntityType.SHEEP) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_MUTTON, 2));
        }

        if (e.getEntityType() == EntityType.RABBIT) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_RABBIT, 1));
        }

        if (e.getEntityType() == EntityType.SPIDER) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.SPIDER_EYE, 1));
            e.getDrops().add(new ItemStack(Material.STRING, 2));
        }

        if (e.getEntityType() == EntityType.SKELETON) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.BONE, 2));
            e.getDrops().add(new ItemStack(Material.ARROW, 3));
        }
    }


}
