package fr.kohei.uhc.game.scenario.impl;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.scenario.AbstractScenario;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * @author Ariloxe
 */
public class Timber extends AbstractScenario implements Listener {
    @Override
    public String getName() {
        return "Timber";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&fLorsque vous minerez un bloc appartenant",
                "&fà un &ctronc d'arbre&f, l'arbre entier",
                "&ftombera alors."
        );
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_AXE;
    }

    @Override
    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UHC.getPlugin());
    }

    private final Map<Location, Integer> limitTimber = new HashMap<>();

    @EventHandler
    public void breakingBlock(BlockBreakEvent e) {
        if (e.isCancelled() && !e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
            return;

        if ((e.getBlock().getType() != Material.LOG))
            return;

        limitTimber.put(e.getBlock().getLocation(), 0);
        breakBlock(e.getBlock(), e.getBlock().getLocation(), e.getPlayer());

    }

    public void breakBlock(Block b, Location model, Player player) {
        JavaPlugin main = UHC.getPlugin();

        if (limitTimber.containsKey(model)) {
            int MAX_TIMER = 90;
            if (limitTimber.get(model) >= MAX_TIMER) {
                limitTimber.remove(model);
                return;

            } else limitTimber.put(model, limitTimber.get(model) + 1);
        } else return;


        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getTypeId(), false);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        b.breakNaturally();

        if (percentChance(5))
            b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.APPLE, 1));

        new BukkitRunnable() {
            public void run() {
                Block above = blockNext(b, 0, 1, 0);
                if (isWoodOrLeaves(above)) {
                    breakBlock(above, model, player);
                }
            }
        }.runTaskLater(main, 1);

        new BukkitRunnable() {
            public void run() {
                Block behind = blockNext(b, 1, 0, 0);
                if (isWoodOrLeaves(behind)) {
                    breakBlock(behind, model, player);
                }
            }
        }.runTaskLater(main, 2);

        new BukkitRunnable() {
            public void run() {
                Block ahead = blockNext(b, -1, 0, 0);
                if (isWoodOrLeaves(ahead)) {
                    breakBlock(ahead, model, player);
                }
            }
        }.runTaskLater(main, 3);

        new BukkitRunnable() {
            public void run() {
                Block left = blockNext(b, 0, 0, 1);
                if (isWoodOrLeaves(left)) {
                    breakBlock(left, model, player);
                }
            }
        }.runTaskLater(main, 4);

        new BukkitRunnable() {
            public void run() {
                Block right = blockNext(b, 0, 0, -1);
                if (isWoodOrLeaves(right)) {
                    breakBlock(right, model, player);
                }
            }
        }.runTaskLater(main, 5);

        new BukkitRunnable() {
            public void run() {
                Block below = blockNext(b, 0, -1, 0);
                if (isWoodOrLeaves(below)) {
                    breakBlock(below, model, player);
                }
            }
        }.runTaskLater(main, 6);
    }

    public boolean isWoodOrLeaves(Block b) {
        Material m = b.getType();
        return m == Material.LOG;
    }

    public Block blockNext(Block b, int x, int y, int z) {
        Location loc = b.getLocation();
        Location above = new Location(b.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
        return above.getBlock();
    }

    private Random r = new Random();
    private int gRi() {
        return r.nextInt(100) + 1;
    }
    public boolean percentChance(int zeroAcent) {
        return gRi() <= zeroAcent;
    }




}
