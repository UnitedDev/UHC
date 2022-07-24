package fr.kohei.uhc.listener;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.config.GameConfiguration;
import fr.kohei.uhc.game.config.RulesManager;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Random;

import static org.bukkit.Material.*;

@RequiredArgsConstructor
public class GameOptionsListener implements Listener {

    private final Plugin plugin;

    private static final Random random = new Random();

    @EventHandler
    public void onBreak2(BlockBreakEvent event) {
        if(!(event.getBlock().getType() == STONE)) return;

        if(event.getBlock().getData() == 0) return;

        event.setCancelled(true);
        event.getBlock().setType(AIR);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(COBBLESTONE));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        final Block b = event.getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);
        final double r = random.nextDouble();
        if (r <= UHC.getInstance().getGameManager().getGameConfiguration().getAppleRate() * 0.01 && b.getType() == Material.LEAVES) {
            b.setType(Material.AIR);
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.APPLE, 1));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent e) {
        final Block block = e.getBlock();
        final Location loc = new Location(block.getWorld(), (block.getLocation().getBlockX() + 0.0f), (block.getLocation().getBlockY() + 0.0f), (block.getLocation().getBlockZ() + 0.0f));
        final double r = random.nextDouble();

        if (r <= UHC.getInstance().getGameManager().getGameConfiguration().getAppleRate() * 0.01 && block.getType() == Material.LEAVES) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(loc, new ItemStack(Material.APPLE));
        }
        if (r <= UHC.getInstance().getGameManager().getGameConfiguration().getFlintRate() * 0.01 && block.getType() == Material.GRAVEL) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(loc, new ItemStack(Material.FLINT));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Enderman)) return;

        event.getDrops().clear();

        final Block b = event.getEntity().getLocation().getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);
        final Random random = new Random();
        final double r = random.nextDouble();
        if (r <= UHC.getInstance().getGameManager().getGameConfiguration().getPearlRate() * 0.01) {
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.ENDER_PEARL, 1));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UPlayer uPlayer = UPlayer.get(player);
        GameConfiguration gameConfiguration = UHC.getInstance().getGameManager().getGameConfiguration();
        switch (event.getBlock().getType()) {
            case DIAMOND_ORE:
                int diamondLimit = gameConfiguration.getDiamondLimit();
                if (diamondLimit == 0) return;
                if (uPlayer.getDiamond() >= diamondLimit) {
                    event.setCancelled(true);
                    event.getBlock().setType(AIR);
                    player.getInventory().addItem(new ItemStack(GOLD_INGOT, 2));
                } else {
                    uPlayer.setDiamond(uPlayer.getDiamond() + 1);
                    player.sendMessage(ChatUtil.prefix("&fVotre &bdiamond limite &fest à &a" + uPlayer.getDiamond() + "&8/&a" + diamondLimit));
                }
                break;
            case GOLD_ORE:
                int goldLimit = gameConfiguration.getGoldLimit();
                if (goldLimit == 0) return;
                if (uPlayer.getGold() >= goldLimit) {
                    event.setCancelled(true);
                    player.sendMessage(ChatUtil.prefix("&fVous avez &cdépassé &fvotre &cgold limit&f."));
                } else {
                    uPlayer.setGold(uPlayer.getGold() + 1);
                    player.sendMessage(ChatUtil.prefix("&fVotre &egold limite &fest à &a" + uPlayer.getDiamond() + "&8/&a" + goldLimit));
                }
                break;

        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        RulesManager manager = UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager();
        Material mat = event.getCurrentItem().getType();
        if (!manager.isDiamondHelmet() && mat == DIAMOND_HELMET) event.setCancelled(true);
        if (!manager.isDiamondChestplate() && mat == DIAMOND_CHESTPLATE) event.setCancelled(true);
        if (!manager.isDiamondLeggings() && mat == DIAMOND_LEGGINGS) event.setCancelled(true);
        if (!manager.isDiamondBoots() && mat == DIAMOND_BOOTS) event.setCancelled(true);
        if (!manager.isFinishRod() && mat == FISHING_ROD) event.setCancelled(true);
        if (!manager.isFlintAndSteel() && mat == FLINT_AND_STEEL) event.setCancelled(true);
    }

    @EventHandler
    public void onEmpty(PlayerBucketEmptyEvent event) {
        RulesManager manager = UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager();

        if (manager.isLavaBucket()) return;
        Block loc = event.getBlockClicked().getRelative(event.getBlockFace());

        boolean b = false;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!(onlinePlayer.getLocation().distance(loc.getLocation()) <= 7)) continue;
            b = true;
            break;
        }

        if (!b) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatUtil.prefix("&cVous ne pouvez pas poser ce block à 5 blocks d'un autre joueur."));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        RulesManager manager = UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager();

        if (event.getItem() == null) return;
        if (event.getItem().getType() == ENDER_PEARL) {
            if (!manager.isEnderPearl()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortal(PortalCreateEvent event) {
        GameConfiguration manager = UHC.getInstance().getGameManager().getGameConfiguration();

        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE) {
            if (!manager.isNether()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        RulesManager manager = UHC.getInstance().getGameManager().getGameConfiguration().getRulesManager();
        if (event.getInventory().getType() == InventoryType.BREWING) {
            if (manager.isPotions()) return;
            event.setCancelled(true);
        }
    }

}
