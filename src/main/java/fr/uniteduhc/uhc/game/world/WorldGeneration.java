package fr.uniteduhc.uhc.game.world;

import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.utils.Title;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

@Getter
@Setter
public class WorldGeneration {

    @Getter
    @Setter
    public static boolean finished;

    @Getter
    @Setter
    private static double percentage = 0D;

    private Double currentChunkLoad;

    private final Double totalChunkToLoad;

    private int cx;

    private int cz;

    private final int radius;

    private final World world;

    public WorldGeneration(World world, int r) {
        r += 150;
        finished = false;
        percentage = 0.0D;
        this.totalChunkToLoad = Math.pow(r, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.cx = -r;
        this.cz = -r;
        this.world = world;
        this.radius = r;
    }

    int b = 0;

    public void load() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 30 && !isFinished(); i++) {
                    Location loc = new Location(world, cx, 0.0D, cz);
                    if (!loc.getChunk().isLoaded())
                        loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ(), true);
                    cx = cx + 16;
                    currentChunkLoad = currentChunkLoad + 1.0D;
                    if (cx > radius) {
                        cx = -radius;
                        cz = cz + 16;
                        if (cz > radius) {
                            currentChunkLoad = totalChunkToLoad;
                            setFinished(true);
                        }
                    }
                }
                percentage = currentChunkLoad / totalChunkToLoad * 100.0D;
                if (isFinished()) {
                    cancel();
                }
            }
        }.runTaskTimer(UHC.getInstance(), 50, 15);
        sendMessage();
    }


    private void sendMessage() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isFinished()) {
                    new Location(world, 0, 100, 0).getChunk().load(true);
                    cancel();
                }
                DecimalFormat format = new DecimalFormat("##.#");
                String pb = getProgressBar(currentChunkLoad.intValue(), totalChunkToLoad.intValue(), 70, '|', ChatColor.GREEN, ChatColor.RED);
                Title.sendActionBar("&cPrégénération &7[" + pb + "&7] &f&l» &e" + format.format(getPercentage()) + "%" + " &8▍ &cTPS &7[&f" + format.format(MinecraftServer.getServer().recentTps[0]) + "&7]");
            }
        }.runTaskTimer(UHC.getInstance(), 0, 10L);
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                        ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }


}