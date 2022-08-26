package fr.uniteduhc.uhc.listener;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.GameState;
import fr.uniteduhc.uhc.game.player.UPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BasicCancelListeners implements Listener {

    private final Plugin plugin;

    @EventHandler
    public void onUnload(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(event.getEntity() instanceof Arrow) return;
        if(event.getEntity() instanceof EnderPearl) return;
        if(event.getEntity() instanceof Item) return;
        if(event.getEntity() instanceof ArmorStand) return;

        if(event.getLocation().getWorld().getName().equals("world")) {
            event.setCancelled(true);
            event.getLocation().getWorld().getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        UPlayer uPlayer = UPlayer.get((Player) event.getWhoClicked());
        if (uPlayer.isEditingDeathInventory() || uPlayer.isEditingStartInventory()) return;
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (UHC.getInstance().getGameManager().getGameState() == GameState.LOBBY || UHC.getInstance().getGameManager().getGameState() == GameState.TELEPORTATION) {
            event.setCancelled(true);
        }
    }

}
